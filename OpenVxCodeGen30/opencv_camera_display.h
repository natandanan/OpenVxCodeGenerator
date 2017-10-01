
#ifndef __opencv_camera_display_h__
#define __opencv_camera_display_h__

#include <opencv2/opencv.hpp>
#include <stdio.h>
#include <string>

#ifndef DEFAULT_VIDEO_SEQUENCE
#define DEFAULT_VIDEO_SEQUENCE "udp://:8554"
#endif

#ifndef DEFAULT_WAITKEY_DELAY
#define DEFAULT_WAITKEY_DELAY  1  /* waitKey delay time in milliseconds after each frame processing */
#endif

class CGuiModule
{
public:
    CGuiModule( const char * captureFile )
        : m_cap( captureFile ? captureFile : DEFAULT_VIDEO_SEQUENCE )
    {
        captureFile  = captureFile ? captureFile : DEFAULT_VIDEO_SEQUENCE;
        m_windowName = captureFile;
        if( !m_cap.isOpened())
        {
            printf( "ERROR: unable to open: %s\n", captureFile );
            exit( 1 );
        }
        printf( "OK: FILE %s %dx%d\n", captureFile, GetWidth(), GetHeight());
        cv::namedWindow(m_windowName);
    }

    CGuiModule( int captureDevice )
        : m_cap( captureDevice )
    {
        char  name[64]; sprintf( name, "CAMERA#%d", captureDevice );
        m_windowName = name;
        if( !m_cap.isOpened())
        {
            printf( "ERROR: CAMERA#%d not available\n", captureDevice );
            exit( 1 );
        }
        printf( "OK: CAMERA#%d %dx%d\n", captureDevice, GetWidth(), GetHeight());
        cv::namedWindow(m_windowName);
    }

    int GetWidth()
    {
        return (int) m_cap.get( CV_CAP_PROP_FRAME_WIDTH );
    }

    int GetHeight()
    {
#if 1 // TBD: workaround for reported OpenCV+Windows bug that returns width instead of height
		return 480;
#else
        return (int) m_cap.get( CV_CAP_PROP_FRAME_HEIGHT );
#endif
    }

    int GetStride()
    {
        return (int) m_imgRGB.step;
    }

    unsigned char * GetBuffer()
    {
        return m_imgRGB.data;
    }

    bool Grab()
    {
        m_cap >> m_imgBGR;
        if( m_imgBGR.empty() )
        {
            return false;
        }
        cv::cvtColor( m_imgBGR, m_imgRGB, cv::COLOR_BGR2RGB );
        return true;
    }

    void DrawText( int x, int y, const char * text )
    {
        cv::putText( m_imgBGR, text, cv::Point( x, y ),
                     cv::FONT_HERSHEY_COMPLEX_SMALL, 0.8, cv::Scalar( 128, 0, 0 ), 1, CV_AA );
    }

    void DrawPoint( int x, int y )
    {
        cv::Point  center( x, y );
        cv::circle( m_imgBGR, center, 1, cv::Scalar( 0, 0, 255 ), 2 );
    }

    void DrawArrow( int x0, int y0, int x1, int y1 )
    {
        DrawPoint( x0, y0 );
        float  dx = (float) ( x1 - x0 ), dy = (float) ( y1 - y0 ), arrow_len = sqrtf( dx * dx + dy * dy );
        if(( arrow_len >= 3.0f ) && ( arrow_len <= 50.0f ) )
        {
            cv::Scalar  color   = cv::Scalar( 0, 255, 255 );
            float       tip_len = 5.0f + arrow_len * 0.1f, angle = atan2f( dy, dx );
            cv::line( m_imgBGR, cv::Point( x0, y0 ), cv::Point( x1, y1 ),                                                                                                              color, 1 );
            cv::line( m_imgBGR, cv::Point( x1, y1 ), cv::Point( x1 - (int) ( tip_len * cosf( angle + (float) CV_PI / 6 )), y1 - (int) ( tip_len * sinf( angle + (float) CV_PI / 6 ))), color, 1 );
            cv::line( m_imgBGR, cv::Point( x1, y1 ), cv::Point( x1 - (int) ( tip_len * cosf( angle - (float) CV_PI / 6 )), y1 - (int) ( tip_len * sinf( angle - (float) CV_PI / 6 ))), color, 1 );
        }
    }

    void Show()
    {
        cv::imshow( m_windowName, m_imgBGR );
    }

    bool AbortRequested()
    {
        char  key = cv::waitKey( DEFAULT_WAITKEY_DELAY );
        if( key == ' ' )
        {
            key = cv::waitKey( 0 );
        }
        if(( key == 'q' ) || ( key == 'Q' ) || ( key == 27 ) /*ESC*/ )
        {
            return true;
        }
        return false;
    }

    void WaitForKey()
    {
        cv::waitKey( 0 );
    }

protected:
    std::string       m_windowName;
    cv::VideoCapture  m_cap;
    cv::Mat           m_imgBGR;
    cv::Mat           m_imgRGB;
};

#endif
