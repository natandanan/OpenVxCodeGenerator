#include <opencv2/opencv.hpp>
#include <stdio.h>
#include <string>
#include <stdio.h>
#include <NVX/nvx.h>
#include <NVX/nvx_opencv_interop.hpp>
#include <opencv2/opencv.hpp>
#include "opencv_camera_display.h"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <fstream>
#include <iostream>
using namespace cv;
using namespace std;

#define ERROR_CHECK_STATUS( status ) { \
        vx_status status_ = (status); \
        if(status_ != VX_SUCCESS) { \
            printf("ERROR: failed with status = (%d) at " __FILE__ "#%d\n", status_, __LINE__); \
            exit(1); \
        } \
    }

#define ERROR_CHECK_OBJECT( obj ) { \
        vx_status status_ = vxGetStatus((vx_reference)(obj)); \
        if(status_ != VX_SUCCESS) { \
            printf("ERROR: failed with status = (%d) at " __FILE__ "#%d\n", status_, __LINE__); \
            exit(1); \
        } \
    }


////////
// log_callback function implements a mechanism to print log messages
// from OpenVX framework onto console. The log_callback function can be
// activated by calling vxRegisterLogCallback API.
void VX_CALLBACK log_callback( vx_context    context,
                   vx_reference  ref,
                   vx_status     status,
                   const vx_char string[] )
{
    printf( "LOG: [ status = %d ] %s\n", status, string );
    fflush( stdout );
}
int main( int argc, char * argv[] )
{
    // Get default video sequence when nothing is specified on command-line and
    // instantiate OpenCV GUI module for reading input RGB images and displaying
    // the image with OpenVX results.
    const char * video_sequence = argv[1];
    CGuiModule gui( video_sequence );

    // Try to grab the first video frame from the sequence using cv::VideoCapture
    // and check if a video frame is available.
    if( !gui.Grab() )
    {
        printf( "ERROR: input has no video\n" );
        return 1;
    }
    vx_uint32  width     = gui.GetWidth();
    vx_uint32  height    = gui.GetHeight();
    vx_uint32  ksize= 7;
	vx_context context = vxCreateContext();
	vx_status status = VX_SUCCESS;
	vx_image input = vxCreateImage(context, width, height, VX_DF_IMAGE_RGB);
	vx_image output = vxCreateImage(context, width, height, VX_DF_IMAGE_RGB);
	vx_graph graph = vxCreateGraph(context);

	vx_image output_colorConvert_IYUV= vxCreateVirtualImage(graph, 0, 0, VX_DF_IMAGE_IYUV);

	vxColorConvertNode(graph, input, output_colorConvert_IYUV);
	vxColorConvertNode(graph, output_colorConvert_IYUV, output);

  // Process the video sequence frame by frame until the end of sequence or aborted.
    for( int frame_index = 0; !gui.AbortRequested(); frame_index++ )
    {
        ////////
        // Copy the input RGB frame from OpenCV to OpenVX.
        // In order to do this, you need to use vxAccessImagePatch and vxCommitImagePatch APIs.
        vx_rectangle_t cv_rgb_image_region;
        cv_rgb_image_region.start_x    = 0;
        cv_rgb_image_region.start_y    = 0;
        cv_rgb_image_region.end_x      = width;
        cv_rgb_image_region.end_y      = height;
        vx_imagepatch_addressing_t cv_rgb_image_layout;
        cv_rgb_image_layout.stride_x   = 3;
        cv_rgb_image_layout.stride_y   = gui.GetStride();
        vx_uint8 * cv_rgb_image_buffer = gui.GetBuffer();
        ERROR_CHECK_STATUS( vxAccessImagePatch( input, &cv_rgb_image_region, 0,
                                                &cv_rgb_image_layout, ( void ** )&cv_rgb_image_buffer, VX_WRITE_ONLY ) );
        ERROR_CHECK_STATUS( vxCommitImagePatch( input, &cv_rgb_image_region, 0,
                                                &cv_rgb_image_layout, cv_rgb_image_buffer ) );
        status = vxVerifyGraph(graph);

        if (status == VX_SUCCESS){
            status = vxProcessGraph(graph);
            if (status == VX_SUCCESS)
            {
                // Display the output image.
                vx_rectangle_t rect = { 0, 0, width, height };
                vx_imagepatch_addressing_t addr = { 0 };
                void * ptr = NULL;
                ERROR_CHECK_STATUS( vxAccessImagePatch( output, &rect, 0, &addr, &ptr, VX_READ_ONLY ) );
                cv::Mat mat( height/2, width, CV_8U, ptr, addr.stride_y );
                cv::imshow( "Example", mat );
                ERROR_CHECK_STATUS( vxCommitImagePatch( output, &rect, 0, &addr, ptr ) );

                ////////
                // Display the results and grab the next input RGB frame for the next iteration.
                char text[128];
//                sprintf( text, "Keyboard ESC/Q-Quit SPACE-Pause [FRAME %d] [ksize %d]", frame_index, ksize );
                gui.DrawText( 0, 16, text );
                gui.Show();
                if( !gui.Grab() )
                {
                    // Terminate the processing loop if the end of sequence is detected.
                    gui.WaitForKey();
                    break;
                }

            }
        }

    }
    return 0;
}