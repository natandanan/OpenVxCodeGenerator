/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLHandling;


import BE.ChannelExtractKernel;
import BE.CodeParameter;
import BE.Connection;
import BE.ConnectionGraph;
import BE.ConvertDepthKernel;
import BE.E_Type;
import BE.E_Channel_Type;
import BE.E_Convert_Policy;
import BE.E_Data_Type;
import BE.E_Image_Type;
import BE.E_Kernels_Name;
import BE.E_Threshold_Type;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;
import BE.ThresholdKernel;
import BE.Kernel;
import BE.DBParameter;
import static BE.E_Kernels_Name.UserKernel;
import BE.ParametersMap;
import BE.UserKernel;
import BE.father_Info;
import java.io.FileInputStream;
import java.io.InputStream;  
import java.io.RandomAccessFile;
import java.net.URL;  
import java.util.Arrays;
import java.util.Scanner;
//import net.sf.json.JSON;  
//import net.sf.json.xml.XMLSerializer;  
//import org.apache.commons.io.IOUtils; 

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


//import sun.misc.IOUtils;
 

public class XMLfunctions {
 
    private static URL url = null;
   // private static InputStream inputStream = null;  

    public static void writeXml(ConnectionGraph graph){
        // according to: https://www.tutorialspoint.com/java_xml/java_dom_create_document.htm
        try {
         DocumentBuilderFactory dbFactory =
         DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = 
            dbFactory.newDocumentBuilder();
         Document doc = dBuilder.newDocument();
        
        // root element
         Element rootElement = doc.createElement("graphs");
         doc.appendChild(rootElement);

         //  graph element
         Element XmlGraph = doc.createElement("graph");
         rootElement.appendChild(XmlGraph);
            
         /*
         // setting attribute to element
         Element width = doc.createElement("picturewidth");
         width.setTextContent(String.valueOf(graph.getGraphInfo().getOutputImageInfo().getWidth()));
         XmlGraph.appendChild(width);
         
         Element height = doc.createElement("pictureheight");
         height.setTextContent(String.valueOf(graph.getGraphInfo().getOutputImageInfo().getHeight()));
         XmlGraph.appendChild(height);*/

         Element outputType = doc.createElement("outputType");
         outputType.setTextContent(String.valueOf(graph.getGraphInfo().getOutputImageInfo().getType()));
         XmlGraph.appendChild(outputType);
         
         Element name = doc.createElement("graphName");
         name.setTextContent(String.valueOf(graph.getGraphInfo().getName()));
         XmlGraph.appendChild(name);
         
        for (Connection c: graph.getConnections()){
            Element connection = doc.createElement("connection");
                            
                Element father= doc.createElement("fatherKernel");
                
                Element fName= doc.createElement("name");
                fName.setTextContent(c.father.getName().toString());
                father.appendChild(fName);
                
                Element fNumber= doc.createElement("number");
                fNumber.setTextContent(String.valueOf(c.father.getNumber()));
                father.appendChild(fNumber);
                
                W_XmlAddingkernelInfo(doc, father, c.father);
                
                if (findRoot(graph.getConnections(),c.father))// for the root of the graph we write also the input parameter
                    father.appendChild(W_XmlRootFatherInputInfo(doc, c.father));
                
                father.appendChild(W_XmlOutputInfo(doc, c.father));
                
            connection.appendChild(father);
                
             
            
            for (Kernel k: c.children){
                
                Element son= doc.createElement("sonKernel");
                
                Element kName= doc.createElement("name");
                kName.setTextContent(k.getName().toString());
                son.appendChild(kName);
                
                Element kNumber= doc.createElement("number");
                kNumber.setTextContent(String.valueOf(k.getNumber()));
                son.appendChild(kNumber);
                
                W_XmlAddingkernelInfo(doc, son, k);
                
                son.appendChild(W_XmlInputInfo(doc, c.father, k));
                
                //if (IsLastKernel(graph.getConnections(), k))
                  //  son.appendChild(W_XmlOutputInfo(doc, k));
                
                connection.appendChild(son);
            }
            XmlGraph.appendChild(connection);
            
            
        }
        
         

         // write the content into xml file
         TransformerFactory transformerFactory =
         TransformerFactory.newInstance();
         Transformer transformer =
         transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
            File file= new File("input1.xml");
         StreamResult result =
         new StreamResult(file);
         transformer.transform(source, result);
         // Output to console for testing
         StreamResult consoleResult =
         new StreamResult(System.out);
         transformer.transform(source, consoleResult);
         Xml2JsonConvert(file);
         
      } 
        catch (ParserConfigurationException | TransformerException | DOMException e) {
      }
    }
    
    public static ConnectionGraph readFromXml(){
        //TODO cover the other options except vx image
        
        // return list of connection Kernels of son and father 
        // in the correct order of the execute kernels
        // according to: https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
        
        // so far I work on tranalation only for one graph, 
        // in order to translate several graph we can add another for loop...

        ConnectionGraph graph = new ConnectionGraph();
        
         try {	
         
         File inputFile = new File("input1.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
    
         
         graph.getGraphInfo().setName(doc.getElementsByTagName("graphName").item(0).getTextContent().trim());
         //graph.getGraphInfo().getOutputImageInfo().setWidth(Integer.parseInt(doc.getElementsByTagName("picturewidth").item(0).getTextContent().trim()));   
         //graph.getGraphInfo().getOutputImageInfo().setHeight(Integer.parseInt(doc.getElementsByTagName("pictureheight").item(0).getTextContent().trim()));
         graph.getGraphInfo().getOutputImageInfo().setType(getEnum_Type(doc.getElementsByTagName("outputType").item(0).getTextContent().trim()));
         
         NodeList nConnectionList = doc.getElementsByTagName("connection");

         for (int conNum = 0; conNum < nConnectionList.getLength(); conNum++) {
            
            Node nNode = nConnectionList.item(conNum);
            Connection newConnection= new Connection();
            
            NodeList nKernelList = nNode.getChildNodes(); // return all the kernels in the connection- fathers and sons
            
            for (int i =0 ; i < nKernelList.getLength(); i++)
            {
                if ("fatherKernel".equals(nKernelList.item(i).getNodeName()))
                {
                   Element eElement = (Element) nKernelList.item(i);
                   Kernel newKernel= getKernelType(eElement);
                   // TODO deal with the data type..
                   // TODO write the enums in the xml in a safer way..
                   
                   
                   if (conNum == 0){ // for the first father who is the only father that haven't been a son

                       newKernel.setName(E_Kernels_Name.valueOf(eElement.getElementsByTagName("name").item(0).getTextContent()));
                       newKernel.setNumber(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
                       
                       R_XmlSetAddingKernelInfo(eElement, newKernel);
                       
                       newKernel.getInputParameters().add(R_XmlGetRootInputParameter(eElement));
                       newKernel.setOutputParameters(R_XmlGetOutputParameters(eElement));                       
                   }
                   else{
                       int kernel_parent_id = Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim());
                       newKernel = graph.getKernelByid(kernel_parent_id);
                       newKernel.setOutputParameters(R_XmlGetOutputParameters(eElement));
                   }
                   newConnection.father= newKernel;
                }
                
                if ("sonKernel".equals(nKernelList.item(i).getNodeName()))
                {
                   Element eElement = (Element) nKernelList.item(i);
                   Kernel newKernel= getKernelType(eElement);  
                   
                   if (graph.getKernelByid(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim())) != null){
                       if (graph.getKernelByid(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim())) instanceof ThresholdKernel ){
//                           newKernel = new ThresholdKernel();
                           newKernel = graph.getKernelByid(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
                        }
                       else if (graph.getKernelByid(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim())) instanceof ConvertDepthKernel){
  //                         newKernel= new ConvertDepthKernel();
                           newKernel = graph.getKernelByid(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
                       }
                       else if (graph.getKernelByid(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim())) instanceof ChannelExtractKernel ){
    //                       newKernel= new ChannelExtractKernel();
                           newKernel = graph.getKernelByid(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
                       }
                       else{
      //                     newKernel= new Kernel();
                           newKernel = graph.getKernelByid(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
                       }  
                   }
//                   else 
  //                     newKernel= new Kernel();
                   
                   newKernel.setName(E_Kernels_Name.valueOf(eElement.getElementsByTagName("name").item(0).getTextContent()));     
                   newKernel.setNumber(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
                   
                   R_XmlSetAddingKernelInfo(eElement, newKernel);
                   
                   NodeList nInputParameters = eElement.getElementsByTagName("inputParameters");
                   NodeList nParameterMapList = nInputParameters.item(0).getChildNodes();
        
                    for (int j =0 ; j < nParameterMapList.getLength(); j++){
                        //Element parameterMapElement = (Element) nParameterMapList.item(j);
                        NodeList nParameterMap = nParameterMapList.item(j).getChildNodes();
                        newKernel.getInputParameters().add(R_XmlGetInputParameters(nParameterMap));
                    }

                   
                   //if (newKernel.getNumber() -2 == conNum)// get the output parameter of the last son 
                       newKernel.setOutputParameters(R_XmlGetOutputParameters(eElement));
                   
                   newConnection.children.add(newKernel);
                }
            }
            graph.getConnections().add(newConnection);        
        }

        System.out.println("----------------------------------------------");
            graph.getConnections().forEach((t) -> {
            System.out.println("Connection "  + ":\n");
            System.out.println("**********************************************");
                    
            System.out.println("Parent : " + t.father.getName() + t.father.toString()+ "\n");
            
            for ( Kernel ch : t.children)
                    System.out.println("Child : " + ch.getName() + ch.toString()+"\n");
            
            });
            System.out.println("----------------------------------------------");
            
            
     } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
     }
       
    return graph;
    }

    private static void W_XmlAddingkernelInfo(Document doc,Element father, Kernel k) {
        if (k instanceof ThresholdKernel){
            Element fThreshDataType= doc.createElement("ThresholdDataType");
             fThreshDataType.setTextContent(String.valueOf((((ThresholdKernel) k).getDataType().toString())));
            father.appendChild( fThreshDataType);

            Element fThreshType= doc.createElement("ThresholdType");
            fThreshType.setTextContent(String.valueOf((((ThresholdKernel) k).getType().toString())));
            father.appendChild(fThreshType);

            if (((ThresholdKernel) k).getType()== E_Threshold_Type.VX_THRESHOLD_TYPE_BINARY){
                Element fThreshValue= doc.createElement("ThresholdValue");
                fThreshValue.setTextContent(String.valueOf(((ThresholdKernel) k).getValue()));
                father.appendChild(fThreshValue);
            }
            else {
                Element fThreshUpper= doc.createElement("ThresholdUpper");
                fThreshUpper.setTextContent(String.valueOf(((ThresholdKernel) k).getUpper()));
                father.appendChild(fThreshUpper);

                Element fThreshLower= doc.createElement("ThresholdLower");
                fThreshLower.setTextContent(String.valueOf(((ThresholdKernel) k).getLower()));
                father.appendChild(fThreshLower);

            }
        }

        else if (k instanceof ConvertDepthKernel){
            Element fConvertPolicy= doc.createElement("ConvertPolicy");
            fConvertPolicy.setTextContent(String.valueOf((((ConvertDepthKernel) k).getConvertPolicy().toString())));
            father.appendChild(fConvertPolicy);

            Element fShiftVal= doc.createElement("ShiftValue");
            fShiftVal.setTextContent(String.valueOf(((ConvertDepthKernel) k).getShift()));
            father.appendChild(fShiftVal);
        }
        else if (k instanceof ChannelExtractKernel){
            Element fChannelType= doc.createElement("ChannelType");
            fChannelType.setTextContent(String.valueOf((((ChannelExtractKernel) k).getChannel().toString())));
            father.appendChild(fChannelType);
        }
            else if (k instanceof UserKernel){
            Element fUserKernelName= doc.createElement("UserKernelName");
            fUserKernelName.setTextContent(String.valueOf((((UserKernel) k).getUserKernelName().toString())));
            father.appendChild(fUserKernelName);
        }
}

    private static Element W_XmlRootFatherInputInfo(Document doc, Kernel k) {
        Element kinputParameters= doc.createElement("inputParameters");
        Element kinputParameter= doc.createElement("inputParameter");

            
            Element pName= doc.createElement("Location");            
            pName.setTextContent(String.valueOf(k.getInputParameters().get(0).getInputParameter().getLocation()));
            kinputParameter.appendChild(pName);

            for (E_Type type :k.getInputParameters().get(0).getInputParameter().getSelectedTypes()){
                Element pType= doc.createElement("Type");
                pType.setTextContent(type.toString());
                kinputParameter.appendChild(pType);
            }

//            if (k.getInputParameters().get(0).inputParameter.getChannelType() != null){
//                Element pChannelType= doc.createElement("ChannelType");
//                pChannelType.setTextContent(String.valueOf(k.getInputParameters().get(0).inputParameter.getChannelType().toString().trim()));
//                kinputParameter.appendChild(pChannelType);
//            }
            kinputParameters.appendChild(kinputParameter);
            return kinputParameters;
        } 

    private static Element W_XmlOutputInfo(Document doc, Kernel k) {
        
        Element kOutputParameters= doc.createElement("outputParameters");
        for (CodeParameter p: k.getOutputParameters()){
            //if (IfAlreadyExist(kOutputParameters, p))// if the current output allready exit- this output is in common use for some children, so we write it only once
              //  break;

            Element kOutputParameter= doc.createElement("outputParameter");
            
            Element pName= doc.createElement("Location");
            pName.setTextContent(String.valueOf(p.getLocation()));
            kOutputParameter.appendChild(pName);
            for (E_Type type :p.getSelectedTypes()){
                Element pType= doc.createElement("Type");
                pType.setTextContent(type.toString());
                kOutputParameter.appendChild(pType);
            }

//            if (p.getChannelType() != null){
//                Element pChannelType= doc.createElement("ChannelType");
//                pChannelType.setTextContent(String.valueOf(p.getChannelType().toString().trim()));
//                kOutputParameter.appendChild(pChannelType);
            //}
            kOutputParameters.appendChild(kOutputParameter);
        }
        return kOutputParameters;
    }

    private static Element W_XmlInputInfo(Document doc, Kernel father, Kernel k) {
        
        Element kInputParameters= doc.createElement("inputParameters");
        for (ParametersMap PM: k.getInputParameters()){
            if (PM.getFatherInfo().getFatherId() == father.getNumber()){
                Element kparametersMap= doc.createElement("parametersMap");

                Element kinputParameter= doc.createElement("inputParameter");

                    Element pName= doc.createElement("Location");
                    pName.setTextContent(String.valueOf(PM.getInputParameter().getLocation()));
                    kinputParameter.appendChild(pName);
                    
                    for (E_Type type :PM.getInputParameter().getSelectedTypes()){
                        Element pType= doc.createElement("Type");
                        pType.setTextContent(type.toString());
                        kinputParameter.appendChild(pType);
                    }

//                    if (PM.inputParameter.getChannelType() != null){
//                        Element pChannelType= doc.createElement("ChannelType");
//                        pChannelType.setTextContent(String.valueOf(PM.inputParameter.getChannelType().toString().trim()));
//                        kinputParameter.appendChild(pChannelType);
//                    }

                    kparametersMap.appendChild(kinputParameter);


                Element kfatherInfo= doc.createElement("fatherInfo");

                    Element pfatherParameter= doc.createElement("fatherParameterLocation");
                    pfatherParameter.setTextContent(String.valueOf(PM.getFatherInfo().getFatherParameterLocation()));
                    kfatherInfo.appendChild(pfatherParameter);

                    Element pfatherNumber= doc.createElement("fatherNumber");
                    pfatherNumber.setTextContent(String.valueOf(PM.getFatherInfo().getFatherId()));
                    kfatherInfo.appendChild(pfatherNumber);

                kparametersMap.appendChild(kfatherInfo);

                kInputParameters.appendChild(kparametersMap);
            }
        }
        return kInputParameters;
    }

    private static ParametersMap R_XmlGetRootInputParameter(Element eElement) {
        NodeList nInputParameter = eElement.getElementsByTagName("inputParameter");
        NodeList ninputParameterContent = nInputParameter.item(0).getChildNodes();

        //Element parameterMapElement = (Element) nParameterMapList.item(j);
        ParametersMap PM= new ParametersMap();


        Element inputParameterElement = (Element) ninputParameterContent;
        PM.setInputParameter(new CodeParameter());
        PM.getInputParameter().setLocation((inputParameterElement.getElementsByTagName("Location").item(0) != null)? 
                Integer.parseInt(inputParameterElement.getElementsByTagName("Location").item(0).getTextContent()): -1 );
        
        int index= 0;
        ArrayList<E_Type> selectedTypes= new ArrayList<E_Type>();
        while(inputParameterElement.getElementsByTagName("Type").item(index) != null){
            selectedTypes.add(getEnum_Type(inputParameterElement.getElementsByTagName("Type").item(index).getTextContent().trim()));
            index++;
        }
        PM.getInputParameter().setSelectedTypes(selectedTypes);
        return PM;
    }

    private static ArrayList<CodeParameter> R_XmlGetOutputParameters(Element eElement) {
        if (!eElement.getElementsByTagName("outputParameters").equals(null)){
            NodeList nOutputParameters = eElement.getElementsByTagName("outputParameters");
            //NodeList nOutputParameterList = nOutputParameters.item(0);//.getChildNodes();
        ArrayList<CodeParameter> outputParameters= new ArrayList<>();

        for(int j =0; j < nOutputParameters.getLength(); j++ ){

            Element outputParameterElement = (Element) nOutputParameters.item(j);
            
            int index= 0;
            ArrayList<E_Type> selectedTypes= new ArrayList<E_Type>();
            while(outputParameterElement.getElementsByTagName("Type").item(index) != null){
                selectedTypes.add(getEnum_Type(outputParameterElement.getElementsByTagName("Type").item(index).getTextContent().trim()));
                index++;
        }// TODO check if the current output already exist in the output list
            outputParameters.add(new CodeParameter(Integer.parseInt(outputParameterElement.getElementsByTagName("Location").item(0).getTextContent()),
                                                selectedTypes));
        }
         return outputParameters;
        }
        return null;

    }

    private static ParametersMap R_XmlGetInputParameters(NodeList nParameterMap) {


        ParametersMap PM= new ParametersMap();

        Element inputParameterElement = (Element) nParameterMap.item(0);
        PM.setInputParameter(new CodeParameter());
        PM.getInputParameter().setLocation((inputParameterElement.getElementsByTagName("Location").item(0) != null)? Integer.parseInt(inputParameterElement.getElementsByTagName("Location").item(0).getTextContent()): -1 );
        int index= 0;
        ArrayList<E_Type> selectedTypes= new ArrayList<E_Type>();
        while(inputParameterElement.getElementsByTagName("Type").item(index) != null){
            selectedTypes.add(getEnum_Type(inputParameterElement.getElementsByTagName("Type").item(index).getTextContent().trim()));
            index++;
        }
        PM.getInputParameter().setSelectedTypes(selectedTypes);

        Element fatherInfoElement = (Element) nParameterMap.item(1);
        PM.setFatherInfo(new father_Info());
        PM.getFatherInfo().setFatherParameterLocation((fatherInfoElement.getElementsByTagName("fatherParameterLocation").item(0) != null)? Integer.parseInt(fatherInfoElement.getElementsByTagName("fatherParameterLocation").item(0).getTextContent()): -1);
        PM.getFatherInfo().setFatherId((fatherInfoElement.getElementsByTagName("fatherNumber").item(0) != null)? Integer.parseInt(fatherInfoElement.getElementsByTagName("fatherNumber").item(0).getTextContent()): 0);

        return PM;
    }

    private static void R_XmlSetAddingKernelInfo(Element eElement, Kernel newKernel) {
        
        if (eElement.getElementsByTagName("ThresholdDataType").item(0) != null){// check threshold 
            ((ThresholdKernel) newKernel).setDataType(E_Data_Type.valueOf(eElement.getElementsByTagName("ThresholdDataType").item(0).getTextContent().trim()));
            if (E_Threshold_Type.VX_THRESHOLD_TYPE_BINARY.toString().equals(eElement.getElementsByTagName("ThresholdType").item(0).getTextContent().trim())){
                 ((ThresholdKernel) newKernel).setType(E_Threshold_Type.VX_THRESHOLD_TYPE_BINARY);
                 ((ThresholdKernel) newKernel).setValue(Integer.parseInt(eElement.getElementsByTagName("ThresholdValue").item(0).getTextContent().trim()));
            }
            else {// range type
                ((ThresholdKernel) newKernel).setType(E_Threshold_Type.VX_THRESHOLD_TYPE_RANGE);
                ((ThresholdKernel) newKernel).setUpper(Integer.parseInt(eElement.getElementsByTagName("ThresholdUpper").item(0).getTextContent().trim()));
                ((ThresholdKernel) newKernel).setLower(Integer.parseInt(eElement.getElementsByTagName("ThresholdLower").item(0).getTextContent().trim()));
            }

        }
        else if (eElement.getElementsByTagName("ConvertPolicy").item(0) != null){// check ConvertDepth
            ((ConvertDepthKernel)newKernel).setConvertPolicy((E_Convert_Policy.valueOf(eElement.getElementsByTagName("ConvertPolicy").item(0).getTextContent().trim()))); 
            ((ConvertDepthKernel)newKernel).setShift(Integer.parseInt(eElement.getElementsByTagName("ShiftValue").item(0).getTextContent().trim()));
        }
        else if (eElement.getElementsByTagName("ChannelType").item(0) != null)// check ChannelExtract
            ((ChannelExtractKernel)newKernel).setChannel((E_Channel_Type.valueOf(eElement.getElementsByTagName("ChannelType").item(0).getTextContent().trim()))); 
        else if (eElement.getElementsByTagName("UserKernelName").item(0) != null)// check user kernel
            ((UserKernel)newKernel).setUserKernelName((eElement.getElementsByTagName("UserKernelName").item(0).getTextContent().trim().toString())); 
        

    }


    private static void Xml2JsonConvert(File file) {
  try{
                String xml = new Scanner(file).useDelimiter("\\Z").next();
                JSONObject jsonObj = XML.toJSONObject(xml);          
//                System.out.println(jsonObj.toString());
                File jsonFile= new File("jsonFile.json");
                RandomAccessFile raf = new RandomAccessFile(jsonFile, "rw");
                raf.setLength(0);

                raf.writeBytes(jsonObj.toString());
                
      
        }
  catch(Exception e){
            e.printStackTrace();
     }
    
    }

    private static E_Type getEnum_Type(String type) {
        if (E_Image_Type.getImageType(type) != null )
            return E_Image_Type.getImageType(type); 
        else if (E_Channel_Type.getChannelType(type) != null )
            return E_Channel_Type.getChannelType(type);
        else 
            return null;
        
        
                                    
    }

    private static Kernel getKernelType(Element eElement) {
        Kernel newKernel;
        if (E_Kernels_Name.valueOf(eElement.getElementsByTagName("name").item(0).getTextContent()) == E_Kernels_Name.Threshold){// check threshold 
            //newConnection.father= new ThresholdKernel();
            newKernel= new ThresholdKernel();                           
        }
        else if (E_Kernels_Name.valueOf(eElement.getElementsByTagName("name").item(0).getTextContent()) == E_Kernels_Name.ConvertDepth){// check ConvertDepth
            //newConnection.father= new ConvertDepthKernel();
            newKernel= new ConvertDepthKernel();
        }
        else if (E_Kernels_Name.valueOf(eElement.getElementsByTagName("name").item(0).getTextContent()) == E_Kernels_Name.channelExtract){
            //newConnection.father= new ChannelExtractKernel();
            newKernel= new ChannelExtractKernel();
        }
        else if (E_Kernels_Name.valueOf(eElement.getElementsByTagName("name").item(0).getTextContent()) == E_Kernels_Name.UserKernel){
            //newConnection.father= new ChannelExtractKernel();
            newKernel= new UserKernel();
        }
        
        else 
            newKernel = new Kernel();
            
        return newKernel;
    }

    private static boolean IfAlreadyExist(Element kOutputParameters, CodeParameter p) {
        NodeList outputList= kOutputParameters.getElementsByTagName("outputParameter");
        for (int i =0; i< outputList.getLength(); i++){
            if (Integer.parseInt(outputList.item(i).getTextContent()) == p.getLocation())
                return true;
        }
        return false;
    }

    private static boolean IsLastKernel(ArrayList<Connection> connections, Kernel k) {
        for (Connection c : connections)
            if (c.father == k)
                return false;
        
        return true;// the current kernel is not a father in any connection = he is the last kernel  
    }

    private static boolean findRoot(ArrayList<Connection> connections, Kernel father) {
        for (Connection c :connections)
            if (father.equals(c.children))
                return false;
        return true;
    }
}