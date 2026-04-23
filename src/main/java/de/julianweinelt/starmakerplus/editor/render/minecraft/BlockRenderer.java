/*
 * Copyright (C) 2026 Julian Weinelt
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package de.julianweinelt.starmakerplus.editor.render.minecraft;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BlockRenderer {
    private static final double SCALE = 10.0;

    private final File cacheDir;

    public BlockRenderer(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    public BufferedImage render(String block) {
        try {
            String blockName = block.replace("minecraft:", "");
            String nameSpace = "minecraft";

            String modelName = resolveModel(blockName);

            File cacheFile = new File(cacheDir, "render/" + nameSpace + "/" + blockName + ".png");
            if (cacheFile.getParentFile().mkdirs()) log.debug("Created cache dir {}", cacheFile.getParentFile().getAbsolutePath());
            if (cacheFile.exists()) {
                return ImageIO.read(cacheFile);
            }

            if (modelName == null) {
                log.warn("Could not resolve model for {}", block);
                return null;
            }

            JsonObject model = loadModelRecursive(modelName, new HashMap<>());
            if (model == null) {
                log.warn("Could not load model for {}", block);
                return null;
            }

            BufferedImage img = renderModel(model);
            ImageIO.write(img, "png", cacheFile);
            return img;

        } catch (Exception e) {
            log.error("Failed to render block", e);
            return null;
        }
    }

    private String resolveModel(String block) throws Exception {
        log.debug("Resolving model for {}", block);
        File file = new File(cacheDir, "blockstates/" + block + ".json");
        if (!file.exists()) return null;

        JsonObject json = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
        if (!json.has("variants")) {
            log.warn("Blockstate {} has no variants", block);
            log.warn("Blockstate: {}", json);
            return null;
        }

        JsonObject variants = json.getAsJsonObject("variants");

        JsonElement entry = variants.entrySet().iterator().next().getValue();

        if (entry.isJsonArray()) {
            JsonArray arr = entry.getAsJsonArray();

            JsonObject chosen = arr.get(0).getAsJsonObject();

            return chosen.get("model").getAsString();
        }

        if (entry.isJsonObject()) {
            return entry.getAsJsonObject().get("model").getAsString();
        }

        return null;
    }

    private JsonObject loadModelRecursive(String modelName, Map<String, String> textures) throws Exception {
        modelName = normalizeModelName(modelName);

        File file = new File(cacheDir, "models/" + modelName + ".json");
        if (!file.exists()) {
            log.warn("Model file does not exist: {}", file.getAbsolutePath());
            return null;
        }

        JsonObject json = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();

        if (json.has("textures")) {
            JsonObject tex = json.getAsJsonObject("textures");
            for (Map.Entry<String, JsonElement> e : tex.entrySet()) {
                textures.put(e.getKey(), e.getValue().getAsString());
            }
        }

        if (json.has("parent")) {
            JsonObject parent = loadModelRecursive(json.get("parent").getAsString(), textures);
            if (parent != null && parent.has("elements")) {
                json.add("elements", parent.get("elements"));
            }
        }

        json.add("resolvedTextures", new Gson().toJsonTree(textures));
        return json;
    }

    private String normalizeModelName(String name) {
        name = name.replace("minecraft:", "");

        if (name.startsWith("block/")) return name;
        if (name.startsWith("item/")) return name;

        return "block/" + name;
    }


    private void drawTexturedQuad(Graphics2D g, BufferedImage tex,
                                  int[] p0, int[] p1, int[] p2, int[] p3) {
        int w = tex.getWidth();
        int h = tex.getHeight();


        drawTexturedTriangle(g, tex,
                0, 0, w, 0, 0, h,
                p0, p1, p3);

        drawTexturedTriangle(g, tex,
                w, 0, w, h, 0, h,
                p1, p2, p3);
    }

    private void drawTexturedTriangle(Graphics2D g, BufferedImage tex,
                                      float sx0, float sy0,
                                      float sx1, float sy1,
                                      float sx2, float sy2,
                                      int[] d0, int[] d1, int[] d2) {


        float dx0 = d0[0], dy0 = d0[1];
        float dx1 = d1[0], dy1 = d1[1];
        float dx2 = d2[0], dy2 = d2[1];


        double det = (sx0 * (sy1 - sy2) + sx1 * (sy2 - sy0) + sx2 * (sy0 - sy1));
        if (Math.abs(det) < 1e-6) return;

        double m00 = (dx0 * (sy1 - sy2) + dx1 * (sy2 - sy0) + dx2 * (sy0 - sy1)) / det;
        double m01 = (dx0 * (sx2 - sx1) + dx1 * (sx0 - sx2) + dx2 * (sx1 - sx0)) / det;
        double m02 = (dx0 * (sx1 * sy2 - sx2 * sy1) + dx1 * (sx2 * sy0 - sx0 * sy2) + dx2 * (sx0 * sy1 - sx1 * sy0)) / det;

        double m10 = (dy0 * (sy1 - sy2) + dy1 * (sy2 - sy0) + dy2 * (sy0 - sy1)) / det;
        double m11 = (dy0 * (sx2 - sx1) + dy1 * (sx0 - sx2) + dy2 * (sx1 - sx0)) / det;
        double m12 = (dy0 * (sx1 * sy2 - sx2 * sy1) + dy1 * (sx2 * sy0 - sx0 * sy2) + dy2 * (sx0 * sy1 - sx1 * sy0)) / det;

        AffineTransform at = new AffineTransform(m00, m10, m01, m11, m02, m12);


        Polygon clip = new Polygon(
                new int[]{d0[0], d1[0], d2[0]},
                new int[]{d0[1], d1[1], d2[1]},
                3
        );

        Shape oldClip = g.getClip();
        g.clip(clip);

        AffineTransform oldTransform = g.getTransform();
        g.setTransform(at);
        g.drawImage(tex, 0, 0, null);
        g.setTransform(oldTransform);

        g.setClip(oldClip);
    }

    private void renderCubeIsometric(Graphics2D g, double[] from, double[] to,
                                     JsonObject faces, JsonObject textures) throws Exception {
        double sx = (to[0] - from[0]) / 16.0;
        double sy = (to[1] - from[1]) / 16.0;
        double sz = (to[2] - from[2]) / 16.0;

        double ox = from[0] / 16.0;
        double oy = from[1] / 16.0;
        double oz = from[2] / 16.0;

        final int SIZE = 96;
        final int ORIGIN_X = 64;
        final int ORIGIN_Y = 80;


        drawFaceLeft(g, getTexture(faces, textures, "west"),
                ox, oz, sx, sz, oy, sy,
                SIZE, ORIGIN_X, ORIGIN_Y);

        drawFaceRight(g, getTexture(faces, textures, "south"),
                ox, oz, sx, sz, oy, sy,
                SIZE, ORIGIN_X, ORIGIN_Y);
        drawFaceFront(g, getTexture(faces, textures, "north"),
                ox, oz, sx, sz, oy, sy, SIZE, ORIGIN_X, ORIGIN_Y);

        drawFaceTop(g, getTexture(faces, textures, "up"),
                ox, oz, sx, sz, oy + sy,
                SIZE, ORIGIN_X, ORIGIN_Y);
    }

    private void drawFaceFront(Graphics2D g, BufferedImage tex,
                               double ox, double oz, double sx, double sz,
                               double oy, double sy,
                               int size, int originX, int originY) {

        if (tex == null) return;

        int[] p0 = project(ox, oy + sy, oz + sz, size, originX, originY);
        int[] p1 = project(ox + sx, oy + sy, oz + sz, size, originX, originY);
        int[] p2 = project(ox + sx, oy, oz + sz, size, originX, originY);
        int[] p3 = project(ox, oy, oz + sz, size, originX, originY);

        drawTexturedQuad(g, tex, p0, p1, p2, p3);
    }

    private int[] project(double x, double y, double z,
                          int size, int originX, int originY) {

        int px = (int) Math.round(originX + (x - z) * (size / 2.0));
        int py = (int) Math.round(originY + (x + z) * (size / 4.0) - y * (size / 2.0));
        return new int[]{px, py};
    }

    private void drawFaceTop(Graphics2D g, BufferedImage tex,
                             double ox, double oz, double sx, double sz, double topY,
                             int size, int originX, int originY) {
        if (tex == null) return;


        int[] p0 = project(ox, topY, oz, size, originX, originY);
        int[] p1 = project(ox + sx, topY, oz, size, originX, originY);
        int[] p2 = project(ox + sx, topY, oz + sz, size, originX, originY);
        int[] p3 = project(ox, topY, oz + sz, size, originX, originY);

        drawTexturedQuad(g, tex, p0, p1, p2, p3);
    }

    private void drawFaceLeft(Graphics2D g, BufferedImage tex,
                              double ox, double oz, double sx, double sz,
                              double oy, double sy,
                              int size, int originX, int originY) {
        if (tex == null) return;


        int[] p0 = project(ox, oy + sy, oz, size, originX, originY);
        int[] p1 = project(ox, oy + sy, oz + sz, size, originX, originY);
        int[] p2 = project(ox, oy, oz + sz, size, originX, originY);
        int[] p3 = project(ox, oy, oz, size, originX, originY);

        drawTexturedQuad(g, tex, p0, p1, p2, p3);
    }

    private void drawFaceRight(Graphics2D g, BufferedImage tex,
                               double ox, double oz, double sx, double sz,
                               double oy, double sy,
                               int size, int originX, int originY) {
        if (tex == null) return;

        int[] p0 = project(ox + sx, oy + sy, oz, size, originX, originY);
        int[] p1 = project(ox + sx, oy + sy, oz + sz, size, originX, originY);
        int[] p2 = project(ox + sx, oy, oz + sz, size, originX, originY);
        int[] p3 = project(ox + sx, oy, oz, size, originX, originY);

        drawTexturedQuad(g, tex, p0, p1, p2, p3);
    }

    private BufferedImage renderModel(JsonObject model) throws Exception {
        BufferedImage canvas = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (!model.has("elements")) {
            g.dispose();
            return canvas;
        }

        JsonArray elements = model.getAsJsonArray("elements");
        JsonObject textures = model.getAsJsonObject("resolvedTextures");

        for (JsonElement el : elements) {
            JsonObject cube = el.getAsJsonObject();
            double[] from = toArray(cube.getAsJsonArray("from"));
            double[] to = toArray(cube.getAsJsonArray("to"));
            renderCubeIsometric(g, from, to, cube.getAsJsonObject("faces"), textures);
        }

        g.dispose();
        return canvas;
    }

    private void renderCube(Graphics2D g, double[] from, double[] to, JsonObject faces, JsonObject textures) throws Exception {
        String[] facesList = new String[]{"down", "up", "north", "south", "west", "east"};

        for (String face : facesList) {
            BufferedImage tex = getTexture(faces, textures, face);
            if (tex == null) continue;

            renderFace(g, face, tex);
        }
    }

    private void renderFace(Graphics2D g, String face, BufferedImage tex) {

        int x = 64;
        int y = 64;

        switch (face) {

            case "up" -> {
                g.drawImage(tex, x - 8, y - 16, 32, 32, null);
            }

            case "down" -> {
                g.drawImage(tex, x - 8, y + 16, 32, 32, null);
            }

            case "north" -> {
                g.drawImage(tex, x - 16, y, 32, 32, null);
            }

            case "south" -> {
                g.drawImage(tex, x + 16, y, 32, 32, null);
            }

            case "west" -> {
                g.drawImage(tex, x - 16, y, 16, 32, null);
            }

            case "east" -> {
                g.drawImage(tex, x + 16, y, 16, 32, null);
            }
        }
    }

    private BufferedImage getTexture(JsonObject faces, JsonObject textures, String face) throws Exception {
        if (!faces.has(face)) return null;

        String tex = faces.getAsJsonObject(face).get("texture").getAsString();
        tex = tex.replace("#", "");

        int maxDepth = 8;
        while (textures.has(tex) && textures.get(tex).getAsString().startsWith("#") && maxDepth-- > 0) {
            tex = textures.get(tex).getAsString().replace("#", "");
        }

        if (!textures.has(tex)) return null;

        String path = textures.get(tex).getAsString().replace("minecraft:", "");

        File file = new File(cacheDir, "textures/" + path + ".png");
        if (!file.exists()) return null;

        return ImageIO.read(file);
    }

    private double[] toArray(JsonArray arr) {
        return new double[]{
                arr.get(0).getAsDouble(),
                arr.get(1).getAsDouble(),
                arr.get(2).getAsDouble()
        };
    }
}