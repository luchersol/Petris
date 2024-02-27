package org.springframework.samples.petris.developers;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.maven.model.Developer;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/developers")


public class DevelopersController {
    List<Developer> developers;

    @GetMapping
    public List<Developer> getDevelopers(){
        if(developers==null)
            loadDevelopers();
        return developers;        
    }

    private void loadDevelopers(){        
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader("pom.xml"));
            developers=model.getDevelopers();                                            
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        
    }


}
