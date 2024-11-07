//
// Created by Jitse van Esch on 2023-08-21.
//

#ifndef nback_h
#define nback_h

#include <stdio.h>

// createNBackString takes an integer array of size, input variables are the array, its size,
// number of diffrent combinatations as a integer, procentesith so 20 equals 20% match
// nback is number of steps between matches.

typedef struct nback_type *Nback;

Nback create(int size, int combinations, int matchPercentage, int nback);
int getIndexOf(Nback s, int index);

// Custom logging
// this can call android logging later
static void (*log_function)(const char *format, va_list args) = NULL;

// Default log function using printf (you can replace this with another logging function)
void default_log_function(const char *format, va_list args) {
    vprintf(format, args);
}

// Function to set the logging function (useful for replacing it later)
void set_log_function(void (*log_fn)(const char *format, va_list args)) {
    log_function = log_fn;
}

// This function calls the current logging function (can be replaced dynamically)
void log_message(const char *format, ...) {
    if (log_function == NULL) {
        // Default to the original printf if no custom log function is set
        log_function = default_log_function;
    }
    va_list args;
    va_start(args, format);
    log_function(format, args);  // Call the current log function
    va_end(args);
}

#endif