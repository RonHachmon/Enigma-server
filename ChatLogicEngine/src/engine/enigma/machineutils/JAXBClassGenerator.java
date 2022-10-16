package engine.enigma.machineutils;

import engine.enigma.jaxb_classes.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;

public class JAXBClassGenerator {
    public JAXBClassGenerator() {
    }

    public static CTEEnigma fromXmlFileToObject(String fileName) throws JAXBException {
        CTEEnigma enigma_machine = null;
        File file = new File(fileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(new Class[]{CTEEnigma.class});
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        enigma_machine = (CTEEnigma) jaxbUnmarshaller.unmarshal(file);
        return enigma_machine;
    }

    public static <T> T unmarshall(String xml, Class<T> clazz)
            throws JAXBException {
        File file = new File(xml);
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        T obj = clazz.cast(unmarshaller.unmarshal(file));
        //T obj = clazz.cast(unmarshaller.unmarshal(new StringReader(xml)));
        return obj;
    }
    public static <T> T unmarshall(InputStream inputStream, Class<T> clazz)
            throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        T obj = clazz.cast(unmarshaller.unmarshal(inputStream));
        return obj;
    }
}

