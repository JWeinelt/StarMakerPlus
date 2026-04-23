package de.julianweinelt.starmakerplus.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
public class ProjectConfiguration {
    private final List<String> lastOpenedFiles;

    private String runDirectory;
    private String projectName;
    private String projectDescription;
}
