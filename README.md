Instructions for running OpenVX Code Generator App on NVIDIA Jetson TX1 (including graphs execution on live stream from GoPro Session):
 
1.	Download the jetpack on the host computer (most have ubuntu 14.04/16.04 os) from here (note the you need to be a member of NVIDIA Developer Program).
2.	connect the usb cable to the jetson tx1 
3.	run from the terminal the run file .
4.	follow the instruction of the jetpack install.
5.	after the flash of the os and all the softwares installed on the jetson follow those instruction to complete the installation of the visionworks and cuda (took from the official README of visionworks):
The following sections contain detailed instructions for the installation of
these packages. These instructions use the following variables:
        o <distro> specifies the OS version (e.g., ubuntu1404).
        o <architecture> specifies the architecture (e.g., amd64. armhf).
        o <ocv_version> specifies the package release version (e.g., 2.4.12.1).
        o <cuda_version> specifies the package release version (e.g., 7.0).
        o <visworks_ver> specifies the VisionWorks version (e.g., 1.0-beta2).
        o <platform> specifies the supported device.

To install CUDA on Jetson TK1 and Jetson TX1 L4T Systems

  Execute the following commands:

        $ sudo dpkg -i cuda-repo-<distro>_<version>_<architecture>.deb
        $ sudo apt-get update
        $ sudo apt-get install cuda-toolkit-<cuda_version>


CUDA Post-Installation Step (all platforms)

  To access the CUDA binaries & libraries from any folder on Linux, add the CUDA
  locations to your system PATH and LD_LIBRARY_PATH environment variables. The
  exact steps depends on whether your target device has a 32-bit or 64-bit CPU.

  For 64-bit systems (eg: most desktop PCs):

    $ echo "# Add 64-bit CUDA library & binary paths:" >> ~/.bashrc
    $ echo "export PATH=/usr/local/cuda-<cuda_version>/bin:$PATH" >> ~/.bashrc
    $ echo "export
      LD_LIBRARY_PATH=/usr/local/cuda-<cuda_version>/lib64:$LD_LIBRARY_PATH" >>
      ~/.bashrc
    $ source ~/.bashrc

To install VisionWorks on Jetson TK1 or Jetson TX1 - L4T system

    $ sudo add-apt-repository universe
    $ sudo dpkg -i libvisionworks-repo_<visworks_ver>_<architecture>.deb
    $ sudo apt-get update
    $ sudo apt-get install libvisionworks libvisionworks-dev libvisionworks-docs

At this point, VisionWorks libraries, headers, samples, and
documentation (and some extra files) are installed to your Jetson or desktop
device.



After VisionWorks installation, use the installed script to copy samples from
/usr/share/visionworks/sources
to a directory with write access and compile them using make:

        $ /usr/share/visionworks/sources/install-samples.sh ~/
        $ cd ~/VisionWorks-<visworks_ver>-Samples/
        $ make



6.	After we able to run vision works program we need the install the java on the jetson:

Installing Java with apt-get is easy. First, update the package index:
sudo apt-get update
	then execute the following command:
	sudo apt-get install default-jre
and then: 
	sudo apt-get install default-jdk
at least don't forget to update the environment variable:
	sudo nano /etc/environment
In this file, add the following line (replacing YOUR_PATH by your Java installation path ):
	JAVA_HOME="YOUR_PATH"
That should be enough to set the environment variable. Now reload this file:
	source /etc/environment
Test it by executing:
	echo $JAVA_HOME
If it returns the just set path, the environment variable has been set successfully. If it doesn't, please make sure you followed all steps correctly.
7.	Now we can run the jar file of our application! Good luck!

Pay attention that the following files exist in home directory of the application: 
Make file
Goprosream2.py
opencv_camera_display.h

Don't forget to turn on the gopro camera and make a wifi connection with it before you run the app.
