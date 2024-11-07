#include "jni.h"
#include "nback.h"
#include "nback.c"
#include <stdarg.h>
#include <android/log.h>

#define TAG "NBackHelper"  // Define a log tag

// Function to replace printf
void android_printf(const char *fmt, ...) {
    va_list args;
    va_start(args, fmt);

    // Use __android_log_print to send output to Logcat
    __android_log_vprint(ANDROID_LOG_DEBUG, TAG, fmt, args);

    va_end(args);
}

void android_vprintf(const char *fmt, va_list args) {
    __android_log_vprint(ANDROID_LOG_DEBUG, TAG, fmt, args);
}

JNIEXPORT jintArray JNICALL
Java_mobappdev_example_nback_1cimpl_NBackHelper_createNBackString(JNIEnv *env, jobject this,
                                                                  jint size, jint combinations,
                                                                  jint matchPercentage, jint nBack) {

    set_log_function(android_vprintf);
    log_message("createNBackString called with size=%d, combinations=%d, matchPercentage=%d, nBack=%d\n",
                   size, combinations, matchPercentage, nBack);

    Nback s1;
    s1 = create(size, combinations, matchPercentage, nBack);

    // Create a new jintArray to store the content array
    jintArray contentArray = (*env)->NewIntArray(env, s1->size);

    // Check if allocation was successful
    if (contentArray == NULL) {
        // Handle allocation failure if needed
        android_printf("Failed to allocate jintArray for content.\n");
        return NULL;
    }

    // Set the content of the jintArray to the content array from the struct
    (*env)->SetIntArrayRegion(env, contentArray, 0, s1->size, s1->content);

    return contentArray;
}