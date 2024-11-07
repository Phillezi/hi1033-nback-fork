#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <jni.h>
#include "nback.h"

#define CONTENT_MAX_SIZE 100

struct nback_type {
    int content[CONTENT_MAX_SIZE];
    int size;
};

// Function to fill in a match (value and corresponding nBackString elements
void fillInAMatch(int nBackString[], int size, int combinations, int nback);
void fillInAMatchV2(int nBackString[], int size, int combinations, int nback);

// Function to fill in all empty elements into nBackString
void fillInAllEmpty(int nBackString[], int size, int combinations, int nback);

/**
 * Function to create N-back string
 * @param nBackString   Array to write the string to
 * @param size          length of the wanted array
 * @param combinations  number of combinations we want (e.g. 3: uses numbers 1-2-3 for the positions)
 * @param matchPercentage  percentage of matches we want to have
 * @param nback         number of how many positions we want between the matching positions (e.g. 3; 1-2-3-1 is a match)
 */
void createNBackString(int nBackString[], int size, int combinations, int matchPercentage, int nback);

Nback create(int size, int combinations, int matchPercentage, int nback){
    // Generate the array to save positions to
    Nback s = (Nback)malloc(sizeof(struct nback_type));
    if (s == NULL) {
        log_message("ERROR: Could not create NBack");
        return NULL;
    }
    s->size = size;
    log_message("NBack: allocated on the heap and size set to: %d", size);
    createNBackString(s->content, size, combinations, matchPercentage, nback);
    return s;
}

int getIndexOf(Nback s, int i){
    return s->content[i];
}


void createNBackString(int nBackString[], int size, int combinations, int matchPercentage, int nback){
    // Seed the random number generator
    srand((unsigned)time(NULL));

    // Initialize all elements in nBackString to 0
    for(int i = 0; i<size; i++){
        nBackString[i] = 0;
    }

    // Calculate number of matches needed based on matchPercentage
    int matches = (int) size * matchPercentage / 100;

    log_message("matches: %d, size: %d\n", matches, size);

    // Fill in the specified number of matches into the nBackString
    for(int i = 0; i<matches; i++){
        fillInAMatch(nBackString, size, combinations, nback);
    }

    // Fill in the remaining empty elements in nBackString
    fillInAllEmpty(nBackString, size, combinations, nback);

}

// Function to fill in a match (value and corresponding nBackString elements)
void fillInAMatch(int nBackString[], int size, int combinations, int nback){
    // Generate a random value for the match
    int value = 1 + rand() % combinations;

    // Continue trying to find a valid position until one is found
    for(; 1 ;){
        // Generate a random position in the nBackString
        int pos = rand() % size;

        // Check if the current and n-back positions are within the bounds
        // and if both positions are currently empty
        if( (pos+nback < size)
            && nBackString[pos] == 0
            && nBackString[pos+nback] == 0
            ){
            // Fill in the match value for both positions
            nBackString[pos] = value;
            nBackString[pos+nback] = value;
            return;
        }

    }
    log_message("Could not fill in match");
}

void fillInAMatchV2(int nBackString[], int size, int combinations, int nback) {

    // Create an array to store the indexes where the value is 0
    int *emptyIndexes = (int *)malloc(size * sizeof(int));
    int emptyCount = 0;

    // Find the indexes where nBackString value is not set
    // And the nback of that index is also not set
    for (int i = nback; i < size; i++) {
        if (nBackString[i] == 0 && nBackString[i-nback] == 0) {
            emptyIndexes[emptyCount] = i;
            emptyCount++;
        }
    }

    // If there are empty positions, randomly select one of them and set it
    if (emptyCount > 0) {
        // Get a random index from the empty indexes
        int randomIndex = rand() % emptyCount;

        // Generate a random value from 1 to combinations
        int value = 1 + rand() % combinations;

        // Set the selected empty position to the generated random value
        nBackString[emptyIndexes[randomIndex]] = value;

        // Optionally log or print the index and value
        // log_message("Filled position %d with value %d", emptyIndexes[randomIndex], value);
    } else {
        // This occurs when we dont have any positions left that satisfy our logic to set a match

        // if there is an empty position get it and set the value of it to the nback value
        for (int i = nback; i < size; i++) {
            if (nBackString[i] == 0) {
                emptyIndexes[emptyCount] = i;
                emptyCount++;
            }
        }
        if (emptyCount > 0) {
            int randomIndex = rand() % emptyCount;
            int index = emptyIndexes[randomIndex];
            nBackString[index] = nBackString[index - nback];    // set it to the value of the nback
                                                                // we know the value at index - nback
                                                                // is set since we wouldve gotten a
                                                                // valid empty position the first time otherwise
        } else {
            log_message("No empty positions available to fill!");
            char values[size*2];
            for (int i = 0; i < size; i++) {
                // If the current position is non-zero, store 'X', otherwise 'O'
                values[i * 2] = (nBackString[i] != 0) ? 'X' : 'O';

                // If it's not the last element, add a comma
                if (i != size - 1) {
                    values[i * 2 + 1] = ',';
                } else {
                    values[i * 2 + 1] = '\0';
                }
            }
            log_message("the n-back string positions: %s\n", values);
        }
    }

    // Free the allocated memory for emptyIndexes array
    free(emptyIndexes);
}

// Function to fill in all empty elements in nBackString
void fillInAllEmpty(int nBackString[], int size, int combinations, int nback){
    for(int i = 0; i< size; i++){
        // Check if the current element is empty
        if(nBackString[i] == 0){
            int test = 0;
            int foo, bar;
            while(test == 0){
                // Generate a random value for the empty element
                int value = 1 + rand() % combinations;

                // Initialize foo and bar flags to 0
                foo = 0;
                bar = 0;

                // Check if the previous n-back element is different from
                // the current value
                if(i-nback > 0){
                    if (nBackString[i-nback] != value){
                        foo = 1;
                    }
                }else{
                    foo = 1;
                }

                // Check if the next n-back element is different from the
                // current value
                if(i+nback < size){
                    if(nBackString[i+nback] != value){
                        bar = 1;
                    }
                }else{
                    bar = 1;
                }

                // If both foo and bar flags are 1, it means the value is different
                // from the previous and next n-back elements, so we can fill in the
                // current element
                if(foo == 1 && bar == 1){
                    test = 1;  // Set the test flag to exit the loop
                    nBackString[i] = value;  // Fill in the value in the current empty element
                }
            }
        }

    }
}
