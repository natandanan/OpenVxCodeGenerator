/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;

/**
 *
 * @author elyasaf
 */
public enum E_Data_Type implements E_Type {
    VX_TYPE_CHAR,
    VX_TYPE_INT8,
    VX_TYPE_UINT8,
    VX_TYPE_INT16,
    VX_TYPE_UINT16,
    VX_TYPE_INT32,
    VX_TYPE_UINT32,
    VX_TYPE_INT64,
    VX_TYPE_UINT64,
    VX_TYPE_FLOAT32,
    VX_TYPE_FLOAT64,
    VX_TYPE_ENUM,
    VX_TYPE_SIZE,
    VX_TYPE_DF_IMAGE,
    VX_TYPE_BOOL ,
    VX_TYPE_SCALAR_MAX,
    VX_TYPE_RECTANGLE,
    VX_TYPE_KEYPOINT,
    VX_TYPE_COORDINATES2D,
    VX_TYPE_COORDINATES3D,
    VX_TYPE_USER_STRUCT_START,
    VX_TYPE_VENDOR_STRUCT_START,
    VX_TYPE_KHRONOS_OBJECT_START,
    VX_TYPE_VENDOR_OBJECT_START,
    VX_TYPE_KHRONOS_STRUCT_MAX,
    VX_TYPE_USER_STRUCT_END,
    VX_TYPE_VENDOR_STRUCT_END,
    VX_TYPE_KHRONOS_OBJECT_END,
    VX_TYPE_VENDOR_OBJECT_END,
    VX_TYPE_REFERENCE,
    VX_TYPE_CONTEXT,
    VX_TYPE_GRAPH,
    VX_TYPE_NODE,
    VX_TYPE_KERNEL,
    VX_TYPE_PARAMETER,
    VX_TYPE_DELAY,
    VX_TYPE_LUT,
    VX_TYPE_DISTRIBUTION,
    VX_TYPE_PYRAMID,
    VX_TYPE_THRESHOLD ,
    VX_TYPE_MATRIX,
    VX_TYPE_CONVOLUTION,
    VX_TYPE_SCALAR,
    VX_TYPE_ARRAY,
    VX_TYPE_IMAGE,
    VX_TYPE_REMAP,
    VX_TYPE_ERROR,
    VX_TYPE_META_FORMAT ,
    VX_TYPE_OBJECT_ARRAY,
}