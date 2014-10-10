#include <stdio.h>
#include <stdlib.h>

/*
INSTRUCTIONS ON PROJECT:
As you read the file, create a linked list of nodes where each node contains both the string and the int value.
If the integer value is -1, that is a sign that you should print the list and then delete all of the
previously inserted items in the list. Continue reading until the file is empty.
Print the list at the end of the programming.
*/

/*struct for the linked list*/
struct node
{
    char String[50];
    int Value;
    struct node* next;
};

void printList(struct node* head); /*function to print list*/
void deleteList(struct node* head); /*function to free the data*/

int main()
{
    int currentInt;
    char currentChar;

    int charCnt = 0, intCnt = 0; /*index references*/

    char charArray[50]; /*giving a max size of 49 to the string*/
    char intArray[12]; /*size of 11 to the integer (i.e. -2,147,483,648 to +2,147,483,647)*/

    struct node* head; /*head node to linked list*/
    head = NULL; /*point to a new node*/

    struct node* traverse; /*node to traverse linked list*/

    FILE* reader;
    reader = fopen("data.txt", "r"); /*open the file*/

    if(!reader) /*if the file is no good, return and say we're done; because it isn't user defined, something is wrong*/
    {
        printf("File reading has crashed and burned... badly.");
        return 0;
    }
    else /*the file is good so do the stuff*/
    {
        /*while not at the end of the file keep reading character by character*/
        while (fscanf(reader,"%c", &currentChar) != EOF)
        {
            if (currentChar == ' ') /*we are at the integer now, handle seperately*/
            {
                /*while the current char is acceptable for building an integer, do that*/
                while (fscanf(reader, "%c", &currentChar) == '+' || currentChar == '-' ||
                       currentChar == '1' || currentChar == '2' || currentChar == '3' ||
                       currentChar == '4' || currentChar == '5' || currentChar == '6' ||
                       currentChar == '7' || currentChar == '8' || currentChar == '9' || currentChar == '0')
                {
                    intArray[intCnt] = currentChar;
                    intCnt++; /*increase the int array index*/
                }
                /*convert the integer array into an integer and stick data in the linked list*/
                intArray[intCnt] = '\0'; /*add the end*/
                charArray[charCnt] = '\0'; /*add the end*/
                sscanf(intArray, "%i", &currentInt); /*convert int char array into 1 integer*/

                if (head != NULL) /*if head is not empty*/
                {
                    while (traverse->next != NULL) /*find the place to insert the new node*/
                    {
                        traverse = traverse->next;
                    }

                    /*since new node location has been found, insert it*/
                    traverse->next = malloc(sizeof(struct node)); /*new node*/
                    traverse = traverse->next;
                    traverse->next = NULL; /*now points to NULL*/
                    strcpy(traverse->String, charArray); /*add the string to the node*/
                    traverse->Value = currentInt; /*add the int value to the node*/
                }
                else /*create head node*/
                {
                    head = malloc(sizeof(struct node)); /*new node*/
                    head->next = NULL;
                    traverse = head;
                    strcpy(traverse->String, charArray); /*add the string to the node*/
                    traverse->Value = currentInt; /*add the int value to the node*/
                }

                if (currentInt == -1)
                {
                    printf("List Being Deleted...\n\n");
                    printList(head);
                    printf("\nList Has Been Deleted...\n\n");
                }

                /*once the integer has been processed, we go to the next line and do the next one,
                but first reset index counters and clear the arrays*/
                intCnt = 0;
                charCnt = 0;
            }
            else /*add to the character array for a string*/
            {
                charArray[charCnt] = currentChar;
                charCnt++; /*increase the character array index*/
            }
        }
    }

    printList(head); /*final print*/

    return 0;
}

void printList(struct node* head)
{
    struct node* printNode;
    printNode = head;

    while (printNode != NULL)
    {
        printf("Name of node: ");
        printf(printNode->String);
        printf("  | ");
        printf("Value of node: %i\n", printNode->Value);
        printNode = printNode->next;
    }

    /*whenever we go to print the list, we need to free all the data, so do that*/
    deleteList(head);
} /*function to print list*/

void deleteList(struct node* head)
{
    struct node* deleter = head;

    while (head != NULL)
    {
        deleter = head;
        head = head->next;
        free(deleter);
    }
} /*function to free the data*/
