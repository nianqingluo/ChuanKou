LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_LDLIBS :=-llog
LOCAL_MODULE    := serial_port  #生成的so库名   lib库名.so
LOCAL_SRC_FILES := serialport.c #编译的源文件

include $(BUILD_SHARED_LIBRARY) #生成库文件类型 动享库