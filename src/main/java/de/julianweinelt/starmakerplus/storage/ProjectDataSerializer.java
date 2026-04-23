package de.julianweinelt.starmakerplus.storage;

import de.julianweinelt.starmakerplus.editor.model.Project;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
public class ProjectDataSerializer {
    public static void serialize(Project project, ProjectConfiguration configuration) {
        try {
            JAXBContext context = JAXBContext.newInstance(ProjectConfiguration.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(configuration, new File(project.getProjectFolderInternal(), "project.xml"));
        } catch (JAXBException e) {
            log.error("Failed to serialize project", e);
        }
    }

    public static ProjectConfiguration deserialize(Project project) {
        try {
            JAXBContext context = JAXBContext.newInstance(ProjectConfiguration.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            ProjectConfiguration user = (ProjectConfiguration) unmarshaller.unmarshal(new File(project.getProjectFolderInternal(), "project.xml"));
        } catch (JAXBException e) {
            log.error("Failed to deserialize project", e);
        }
        return null;
    }
}
