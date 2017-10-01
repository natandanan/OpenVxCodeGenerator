

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeGeneration;

import BE.Connection;
import BE.ConnectionGraph;
import BE.ConvertDepthKernel;
import BE.DB_Kernels;
import BE.E_Convert_Policy;
import BE.E_Image_Type;
import BE.Kernel;
import BE.ParametersMap;
import BE.ThresholdKernel;
import BE.ChannelExtractKernel;
import BE.E_IO;
import BE.E_Kernels_Name;
import BE.UserKernel;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.util.Scanner;
/**
 *
 * @author elyasaf
 */
public class XML2code {
    private static XML2code instance = null;// DB singleton instance

    static private ArrayList<String> declerationLines; 
    static private ArrayList<String> KernelscallLines;
    static private ArrayList<String> allVarsName;
    static private RandomAccessFile raf;
    //Natan add
    static private DB_Kernels db_kernels = DB_Kernels.getInstance();

    
    
    public XML2code(){
        allVarsName= new  ArrayList<>();
        declerationLines= new ArrayList<>();
        KernelscallLines= new ArrayList<>();
        try{
            String fileName = "openVxCode.cpp";
            File f= new File(fileName);
            raf = new RandomAccessFile(f, "rw");
            raf.setLength(0);
        }
        catch (IOException e) {
        }
        
    }
    
    public  static XML2code getInstance() {
      if(instance == null) {
         instance = new XML2code();
      }
      return instance;
   }
    
    public static void codeGenerator(ConnectionGraph graph) {
    {
        try{
        
            updateVarsName(graph.getConnections());
            
            basicInitializeLines(graph);
            
            raf.writeBytes("\n");

            // adding to the lists the rlevance lines
            for (Connection c: graph.getConnections()){                 
                
                    KernelsCode(c.father);
                    if (graph.getConnections().get(graph.getConnections().size()-1).equals(c))
                            KernelsCode(c.children.get(0));
            }
            
            // writing lists content in the file output
            for (String line: declerationLines)
                raf.writeBytes(line);
            
            raf.writeBytes("\n");
            
            for (String line: KernelscallLines)
                raf.writeBytes(line);
            
            processGraphLines(graph.getGraphInfo().getName());
            
            raf.close();
            
            
        } catch (IOException e) {
        }
        
    }
  }

    // scan the graph.list and give nick names for i\o in the code
    private static void updateVarsName(ArrayList<Connection> list) {
     
        int varLocation= 0;
        String varName;
        
        // in the first connection the father kernels get input var name= input 
//        list.get(0).father.getInputVariablesNames().put(1,"input");
        
        
        for (Connection c : list)
        {
             if (ConnectionGraph.findRoot(list, c.father))// for the root kernel the input var name is input
                    c.father.getInputVariablesNames().put(1,"input");
         
            for (Kernel k: c.children){
                      for (ParametersMap InInfo: k.getInputParameters()){
                    if (c.father.getNumber() == InInfo.getFatherInfo().getFatherId()){// only if the current parameter is from the current son we update the lists
                        varLocation= InInfo.getFatherInfo().getFatherParameterLocation();
                        if (c.father.getOutputVariablesNames().get(varLocation) != null){
                            k.getInputVariablesNames().put(InInfo.getInputParameter().getLocation(), c.father.getOutputVariablesNames().get(varLocation));
                            continue;
                        }
                        varName= getParameterName(c.father);
                        c.father.getOutputVariablesNames().put(varLocation,varName);
                        varLocation= InInfo.getInputParameter().getLocation();
                        k.getInputVariablesNames().put(varLocation, varName);
                        
                        if (c == list.get(list.size()-1)){// this is the last connection
                            varLocation=  db_kernels.getParamLocationByParamName(k.getName(), db_kernels.getVarsNameListByKernelName(k.getName(), E_IO.output).get(0));//k.getOutputParameters().get(0).getLocation();
                            varName= "output";
                            k.getOutputVariablesNames().put(varLocation,varName);
                        }
                    }
                }
            }
        }    
    }
 
    private static void basicInitializeLines(ConnectionGraph graph) {
        try {
            
            raf.writeBytes("#include <opencv2/opencv.hpp>\n" +
"#include <stdio.h>\n" +
"#include <string>\n" +
"#include <stdio.h>\n" +
"#include <NVX/nvx.h>\n" +
"#include <NVX/nvx_opencv_interop.hpp>\n" +
"#include <opencv2/opencv.hpp>\n" +
"#include \"opencv_camera_display.h\"\n" +
"#include \"opencv2/imgproc/imgproc.hpp\"\n" +
"#include \"opencv2/highgui/highgui.hpp\"\n" +
"#include <fstream>\n" +
"#include <iostream>\n" +
"using namespace cv;\n" +
"using namespace std;\n");

            raf.writeBytes("\n");
            
            raf.writeBytes("#define ERROR_CHECK_STATUS( status ) { \\\n" +
                "        vx_status status_ = (status); \\\n" +
                "        if(status_ != VX_SUCCESS) { \\\n" +
                "            printf(\"ERROR: failed with status = (%d) at \" __FILE__ \"#%d\\n\", status_, __LINE__); \\\n" +
                "            exit(1); \\\n" +
                "        } \\\n" +
                "    }\n" +
                "\n" +
                "#define ERROR_CHECK_OBJECT( obj ) { \\\n" +
                "        vx_status status_ = vxGetStatus((vx_reference)(obj)); \\\n" +
                "        if(status_ != VX_SUCCESS) { \\\n" +
                "            printf(\"ERROR: failed with status = (%d) at \" __FILE__ \"#%d\\n\", status_, __LINE__); \\\n" +
                "            exit(1); \\\n" +
                "        } \\\n" +
                "    }");
            
            raf.writeBytes("\n"); 
            
            for (Connection con : graph.getConnections())
                for (Kernel k: con.children)
                    if (k.getName().equals(E_Kernels_Name.UserKernel)){
                        readWrite_userKernelCode(((UserKernel) k));
                        raf.writeBytes("\n"); 
            
                    }
            raf.writeBytes("\n");
            
            raf.writeBytes("\n////////\n" +
                "// log_callback function implements a mechanism to print log messages\n" +
                "// from OpenVX framework onto console. The log_callback function can be\n" +
                "// activated by calling vxRegisterLogCallback API.\n" +
                "void VX_CALLBACK log_callback( vx_context    context,\n" +
                "                   vx_reference  ref,\n" +
                "                   vx_status     status,\n" +
                "                   const vx_char string[] )\n" +
                "{\n" +
                "    printf( \"LOG: [ status = %d ] %s\\n\", status, string );\n" +
                "    fflush( stdout );\n" +
                "}"); 

            raf.writeBytes("\nint main( int argc, char * argv[] )\n" +
                "{\n" +
                "    // Get default video sequence when nothing is specified on command-line and\n" +
                "    // instantiate OpenCV GUI module for reading input RGB images and displaying\n" +
                "    // the image with OpenVX results.\n" +
                "    const char * video_sequence = argv[1];\n" +
                "    CGuiModule gui( video_sequence );\n" +
                "\n" +
                "    // Try to grab the first video frame from the sequence using cv::VideoCapture\n" +
                "    // and check if a video frame is available.\n" +
                "    if( !gui.Grab() )\n" +
                "    {\n" +
                "        printf( \"ERROR: input has no video\\n\" );\n" +
                "        return 1;\n" +
                "    }\n" +
                "    vx_uint32  width     = gui.GetWidth();\n" +
                "    vx_uint32  height    = gui.GetHeight();\n"+
                "    vx_uint32  ksize= 7;\n"); 
            

            raf.writeBytes("\tvx_context context = vxCreateContext();\n");

            raf.writeBytes("\tvx_status status = VX_SUCCESS;\n");

            for (Connection c: graph.getConnections())
                if (ConnectionGraph.findRoot(graph.getConnections(), c.father)){// for the root kernel the input var name is input
                    raf.writeBytes("\tvx_image input = vxCreateImage(context,"
                            + " width,"
                            + " height, "
                            + c.father.getInputParameters().get(0).getInputParameter().getPrameterType("E_Image_Type").toString().trim()
                            + ");\n");
                }
            
            raf.writeBytes("\tvx_image output = vxCreateImage(context,"
                    + " width,"
                    + " height, "
                    + graph.getGraphInfo().getOutputImageInfo().getType().toString().trim()
                    + ");\n");

            raf.writeBytes("\tvx_graph graph = vxCreateGraph(context);\n");

        }
        catch (Exception e) {
            e.printStackTrace();
        }    
    }
      
    private static void KernelsCode(Kernel k) {
        try {
            
            String KernelName= k.getName().toString();
                    
                    switch (KernelName.trim()) {
                        case "AbsDiff":
                            AbsDiffLines(k);
                            break;
                        case "AccumulateImage":
                            AccumulateImageLines(k);
                            break;
                        case "AccumulateSquareImage":
                            AccumulateSquareImageLines(k);
                            break;
                        case "AccumulateWeightedImage":
                            AccumulateWeightedImageLines(k);
                            break;
                        case "Add":
                            AddLines(k);
                            break;
                        case "colorConvert":  
                            colorConvertLines(k);
                            break;
                        case "channelExtract":
                            channelExtractLines((ChannelExtractKernel) k);
                            break;
                        case "EqualizeHist":  
                            EqualizeHistLines(k);
                            break;
                        case "ChannelCombine":
                            ChannelCombineLines(k);
                            break;
                        case "Sobel3x3":
                            Sobel3x3Lines(k);
                            break;
                        case "Magnitude":
                            MagnitudeLines(k);
                            break;
                        case "ConvertDepth":  
                            ConvertDepthLines((ConvertDepthKernel) k);
                            break;
                        case "Gaussian3x3":
                            Gaussian3x3Lines(k);
                            break;
                        case "Threshold":
                            ThresholdLines((ThresholdKernel) k);
                            break;
                        case "Phase":
                            PhaseLines(k);
                            break;
                        case "UserKernel":
                            UserKernelLines((UserKernel) k);
                            break;
                        
                        default: 
                            break;           
            }    
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        }
 
    private static void AbsDiffLines(Kernel kernel) {
        
        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(3).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 3);
                
            
            
            currLine= "\tvxAbsDiffNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getInputVariablesNames().get(2) +
                    ", " +
                    kernel.getOutputVariablesNames().get(3).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
                      e.printStackTrace();       
          }    
    }

    private static void AccumulateImageLines(Kernel kernel) {
        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(2).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 2);

            
            currLine= "\tvxAccumulateImageNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getOutputVariablesNames().get(2).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
                      e.printStackTrace();       
          }         

    }

    private static void AccumulateSquareImageLines(Kernel kernel) {
        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(3).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 3);

            currLine= "\tvxColorConvertNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getOutputVariablesNames().get(2).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
                      e.printStackTrace();       
          }    

    }

    private static void AccumulateWeightedImageLines(Kernel k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void AddLines(Kernel k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static void colorConvertLines(Kernel kernel) {

        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(2).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 2);

            
            currLine= "\tvxColorConvertNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getOutputVariablesNames().get(2).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
                      e.printStackTrace();       
          }    
    }

    private static void channelExtractLines(ChannelExtractKernel kernel) {

        String currLine;
        try{            
                    
            if (! kernel.getOutputVariablesNames().get(3).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 3);

                        
            currLine= "\tvxChannelExtractNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getChannel().toString().trim()+
                    ", " +
                    kernel.getOutputVariablesNames().get(3).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
          }    
    }

    private static void EqualizeHistLines(Kernel kernel) {
    
        
        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(2).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 2);

            
            currLine= "\tvxEqualizeHistNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getOutputVariablesNames().get(2).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
          }
    }

    private static void ChannelCombineLines(Kernel kernel) {     

        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(5).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 5);

            
            currLine= "\tvxChannelCombineNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " ;
            
           if (kernel.getInputVariablesNames().size() == 1 ){
                currLine+= kernel.getInputVariablesNames().get(1)+
                        ", "+
                        kernel.getInputVariablesNames().get(1)+
                        ", NULL, ";
                currLine+= kernel.getOutputVariablesNames().get(5);
            
            
                currLine+= ");\n";
                
                KernelscallLines.add(currLine);
                
                return;
                
           }
           else
                currLine+= kernel.getInputVariablesNames().get(2)+
                        ", ";

            
            if (!(kernel.getInputVariablesNames().size() > 2) )
                currLine+= "NULL, ";
            else
                currLine+= kernel.getInputVariablesNames().get(3)+
                        ", ";
            if (!(kernel.getInputVariablesNames().size() > 3) )
                currLine+= "NULL, ";
            else
                currLine+= kernel.getInputVariablesNames().get(4);
            
            
            currLine+= kernel.getOutputVariablesNames().get(5);
            
            
            currLine+= ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
          }
       }

    private static void Sobel3x3Lines(Kernel kernel) {
        String currLine;
        try{
            
            declerationLine(kernel, 2);
            declerationLine(kernel, 3);
            
            
            currLine= "\tvxSobel3x3Node(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getOutputVariablesNames().get(2).trim()+
                    ", " +
                    kernel.getOutputVariablesNames().get(3).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
          }

    }

    private static void MagnitudeLines(Kernel kernel) {
        
        String currLine;
        try{            
                    
            if (! kernel.getOutputVariablesNames().get(3).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 3);

                        
            currLine= "\tvxMagnitudeNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getInputVariablesNames().get(2) +
                    ", " +
                    kernel.getOutputVariablesNames().get(3).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
          }    

    }

    private static void ConvertDepthLines(ConvertDepthKernel kernel) {
        int shift= kernel.getShift();
        E_Convert_Policy convertPolicy= kernel.getConvertPolicy();
        String currLine;
        try{            
                    
            if (! kernel.getOutputVariablesNames().get(2).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
            {
                currLine= "\tvx_int32 shiftVal = "+
                        String.valueOf(shift)+ ";\n" ;
                declerationLines.add(currLine);

                currLine= "\tvx_scalar shift = vxCreateScalar(context, VX_TYPE_INT32, &shiftVal);\n";
                declerationLines.add(currLine);
                declerationLine(kernel, 2);

            }
                        
            currLine= "\tvxConvertDepthNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getOutputVariablesNames().get(2).trim()+
                    ", ";
            currLine+= convertPolicy.toString();
                    
            currLine+= ", shift);\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
          }    


    }

    private static void Gaussian3x3Lines(Kernel kernel) {
        
        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(2).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 2);
            
            
            
            currLine= "\tvxGaussian3x3Node(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getOutputVariablesNames().get(2).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
                      e.printStackTrace();       
          }
    }

    private static void ThresholdLines(ThresholdKernel kernel) {
        String ThresholdName;
        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(3).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 3);

            
            ThresholdName= InitialThresholdAttributes( kernel);
            
            currLine= "\tvxThresholdNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    ThresholdName+
                    ", "+
                    kernel.getOutputVariablesNames().get(3).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
                      e.printStackTrace();       
          }
        

    }
    
    private static void PhaseLines(Kernel kernel) {

        String currLine;
        try{
            
            if (! kernel.getOutputVariablesNames().get(3).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(kernel, 3);

            
            currLine= "\tvxPhaseNode(graph, "+
                    kernel.getInputVariablesNames().get(1) +
                    ", " +
                    kernel.getInputVariablesNames().get(2) +
                    ", " +
                    kernel.getOutputVariablesNames().get(3).trim()+
                    ");\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
                      e.printStackTrace();       
          }    
    }
    
    private static void UserKernelLines(UserKernel userKernel) {
        String currLine;
        try{
            
            if (! userKernel.getOutputVariablesNames().get(2).trim().equals("output"))// the current kernel is not the final output, might be not necessery because we can write the output withe all the other kernels output
                declerationLine(userKernel, 2);

            currLine= "\tERROR_CHECK_STATUS( registerUserKernel( context ) )";
            currLine+= "\tuser"+userKernel.getUserKernelName()+"Node(graph, "+
                    userKernel.getInputVariablesNames().get(1) +
                    ", " +
                    userKernel.getOutputVariablesNames().get(2).trim()+
                    ", ksize);\n";
            
            KernelscallLines.add(currLine);
        }

          catch (Exception e) {
                      e.printStackTrace();       
          }    
    
    }

    
    private static void processGraphLines(String graphName) {
        try{
            
            raf.writeBytes("\n  // Process the video sequence frame by frame until the end of sequence or aborted.\n" +
                "    for( int frame_index = 0; !gui.AbortRequested(); frame_index++ )\n" +
                "    {\n" +
                "        ////////\n" +
                "        // Copy the input RGB frame from OpenCV to OpenVX.\n" +
                "        // In order to do this, you need to use vxAccessImagePatch and vxCommitImagePatch APIs.\n" +
                "        vx_rectangle_t cv_rgb_image_region;\n" +
                "        cv_rgb_image_region.start_x    = 0;\n" +
                "        cv_rgb_image_region.start_y    = 0;\n" +
                "        cv_rgb_image_region.end_x      = width;\n" +
                "        cv_rgb_image_region.end_y      = height;\n" +
                "        vx_imagepatch_addressing_t cv_rgb_image_layout;\n" +
                "        cv_rgb_image_layout.stride_x   = 3;\n" +
                "        cv_rgb_image_layout.stride_y   = gui.GetStride();\n" +
                "        vx_uint8 * cv_rgb_image_buffer = gui.GetBuffer();\n" +
                "        ERROR_CHECK_STATUS( vxAccessImagePatch( input, &cv_rgb_image_region, 0,\n" +
                "                                                &cv_rgb_image_layout, ( void ** )&cv_rgb_image_buffer, VX_WRITE_ONLY ) );\n" +
                "        ERROR_CHECK_STATUS( vxCommitImagePatch( input, &cv_rgb_image_region, 0,\n" +
                "                                                &cv_rgb_image_layout, cv_rgb_image_buffer ) );\n" +
                "        status = vxVerifyGraph(graph);\n" +
                "\n" +
                "        if (status == VX_SUCCESS){\n" +
                "            status = vxProcessGraph(graph);\n" +
                "            if (status == VX_SUCCESS)\n" +
                "            {\n" +
                "                // Display the output image.\n" +
                "                vx_rectangle_t rect = { 0, 0, width, height };\n" +
                "                vx_imagepatch_addressing_t addr = { 0 };\n" +
                "                void * ptr = NULL;\n" +
                "                ERROR_CHECK_STATUS( vxAccessImagePatch( output, &rect, 0, &addr, &ptr, VX_READ_ONLY ) );\n" +
                "                cv::Mat mat( height/2, width, CV_8U, ptr, addr.stride_y );\n" +
                "                cv::imshow( \""
                    +  graphName
                    + "\", mat );\n" +
                "                ERROR_CHECK_STATUS( vxCommitImagePatch( output, &rect, 0, &addr, ptr ) );\n" +
                "\n" +
                "                ////////\n" +
                "                // Display the results and grab the next input RGB frame for the next iteration.\n" +
                "                char text[128];\n" +
                "//                sprintf( text, \"Keyboard ESC/Q-Quit SPACE-Pause [FRAME %d] [ksize %d]\", frame_index, ksize );\n" +
                "                gui.DrawText( 0, 16, text );\n" +
                "                gui.Show();\n" +
                "                if( !gui.Grab() )\n" +
                "                {\n" +
                "                    // Terminate the processing loop if the end of sequence is detected.\n" +
                "                    gui.WaitForKey();\n" +
                "                    break;\n" +
                "                }\n" +
                "\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "    return 0;\n"+
                "}");
            /*
            raf.writeBytes("\n");
            raf.writeBytes("\tstatus = vxVerifyGraph(graph);\n"+
                            "\tif (status == VX_SUCCESS){\n" +
                            "\t\tstatus = vxProcessGraph(graph);\n" +
                            "\t\tif (status == VX_SUCCESS)\n" +
                            "\t\t{\n" +
                            "\t\t\treturn output;\n" +
                            "\t\t}\n" +
                            "\t}\n" +
                            "\treturn NULL;\n"+
                            "}");
            */
            }
                
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getParameterName(Kernel k) {
                
        String varName;
                
        varName = "output_"+ k.getName().toString().trim();
        if (! k.getOutputParameters().get(0).getPrameterType("E_Image_Type").equals(E_Image_Type.VX_DF_IMAGE_VIRT))
            varName= varName + '_' + k.getOutputParameters().get(0).getPrameterType("E_Image_Type").toString().replace("VX_DF_IMAGE_", "").trim();
        
        //Check if exist - if exist add number to var name
        for (int j =0; j <= allVarsName.size(); j++ ){    

            if (allVarsName.isEmpty() || (! allVarsName.isEmpty() && ! allVarsName.contains(varName)))
            {
                allVarsName.add(varName);                        
                break;
            }
            else if (! allVarsName.contains(varName +'_' + j))
            {
                varName= varName + '_' + j;
                allVarsName.add(varName);
                break;
            }
        }

       
        return varName;

    }

    private static String InitialThresholdAttributes(ThresholdKernel kernel) {
        String thresholdName="";
        String currLine;
        String Type= kernel.getType().toString();//  get the threshold type- range or boolean
        String DataType= kernel.getDataType().toString();   
        int upper= kernel.getUpper();//  get the upper and lower data or binary
        int lower= kernel.getLower();//
        int binaryValue= kernel.getValue();
        
        thresholdName= "t";
        for (int j =0; j <= allVarsName.size(); j++ ){    

            if (allVarsName.isEmpty() || (! allVarsName.isEmpty() && ! allVarsName.contains(thresholdName)))
            {
                allVarsName.add(thresholdName);                        
                break;
            }
            else if (! allVarsName.contains(thresholdName +'_' + j))
            {
                allVarsName.add(thresholdName + '_'+ j);
                break;
            }
        }

        
        
        currLine= "\tvx_threshold "+
                thresholdName+
                "= vxCreateThreshold(context, "+
                Type+
                ", "+
                DataType+
                ");\n";
        
        declerationLines.add(currLine);
        
        
        
        if (Type == "VX_THRESHOLD_TYPE_BINARY"){
            currLine= "\t"
                    + "vx_int32 binaryValue = "+
                binaryValue+ ";\n";
            declerationLines.add(currLine);
            currLine= "\tvxSetThresholdAttribute("+
                     thresholdName+
                      ", VX_THRESHOLD_THRESHOLD_VALUE, &binaryValue," +
                        "sizeof(binaryValue));\n";
            declerationLines.add(currLine);
        }
        else{
            currLine= "\tvx_int32" +
                    " upper = "+
                upper+
                ", lower = "+
                lower+
                ";\n";
            declerationLines.add(currLine);
            currLine= "\tvxSetThresholdAttribute("+
                    thresholdName+
                    ", VX_THRESHOLD_ATTRIBUTE_THRESHOLD_UPPER, &upper," +
                        "sizeof(upper));\n" +
                        "\tvxSetThresholdAttribute("
                    + thresholdName
                    + ", VX_THRESHOLD_ATTRIBUTE_THRESHOLD_LOWER, &lower," +
                        "sizeof(lower));\n";
            declerationLines.add(currLine);
        }
        
        
        return thresholdName;
        
    }
    
    private static void readWrite_userKernelCode(UserKernel k) {
        try{
            ArrayList<String> functionsFiles = new ArrayList<>();
            functionsFiles.add("user" + k.getUserKernelName() + "Node"+ "_"+ k.getNumber());
            functionsFiles.add(k.getUserKernelName()+"InputValdiator"+ "_"+ k.getNumber());
            functionsFiles.add(k.getUserKernelName()+"OutputValdiator"+ "_"+ k.getNumber());
            functionsFiles.add(k.getUserKernelName()+"HostSideFunction"+ "_"+ k.getNumber());
            functionsFiles.add("registerUserKernel"+ "_"+ k.getNumber());
            
             for (String ffile : functionsFiles) {
                File file = new File(ffile);
                String fileContent = new Scanner( file).useDelimiter("\\Z").next();
                raf.writeBytes(fileContent);
                raf.writeBytes("\n");
                
             }
            
        }
        catch (Exception e) {
                      e.printStackTrace();       
          } 
    }

    private static void declerationLine(Kernel kernel, int varLocation) {
        
        String currLine = "\tvx_image "+
                kernel.getOutputVariablesNames().get(varLocation)+
                "= vxCreateVirtualImage(graph, 0, 0, "+
                kernel.getOutputParameters().get(0).getPrameterType("E_Image_Type").toString().trim() +
                ");\n";

        declerationLines.add(currLine);    
    }

    /**
     * @return the declerationLines
     */
    public ArrayList<String> getDeclerationLines() {
        return declerationLines;
    }

    /**
     * @param declerationLines the declerationLines to set
     */
    public void setDeclerationLines(ArrayList<String> declerationLines) {
        this.declerationLines = declerationLines;
    }

    /**
     * @return the KernelscallLines
     */
    public ArrayList<String> getKernelscallLines() {
        return KernelscallLines;
    }

    /**
     * @param KernelscallLines the KernelscallLines to set
     */
    public void setKernelscallLines(ArrayList<String> KernelscallLines) {
        this.KernelscallLines = KernelscallLines;
    }
}