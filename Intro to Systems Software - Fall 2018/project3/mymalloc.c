#include <stdlib.h> 
#include <unistd.h> 
#include <stdio.h> 
#include <assert.h>

//Landon Higinbotham

struct Block {
	int occ; // whether block is occupied
	int size; // size of block (including header)
	struct Block *prev; // pointer to previous block
	struct Block *next; // pointer to next block

};

static struct Block *head = NULL; // head of list 

/** @brief Bump up program break by the allocation size + block header size
* and fill in the block header fields
*
* Now blocks are added to the double-linked list which starts
* from 'head'.  When a new block is added, the occ bit needs to
* be set to 1, size needs to be set to the block size, and the
* link pointers need to be updated such that it is double-linked
* to the previous block.
*
* @param size Allocation size in bytes.
* @return Pointer to the usable part of allocated memory.
*/

void *my_malloc(int size)
{
	struct Block *bestfit = NULL;
	if (head == NULL) //Creates the head of the linked list.
	{
		struct Block *bloc = (struct Block*) sbrk(sizeof(struct Block));
		bloc->occ = 1;
		bloc->size = size + sizeof(struct Block);
		head = bloc;
	}
	else
	{
		struct Block *cur = head;
		while (cur->next != NULL) //Goes through the list to see if there is a possible Best Fit.
		{

			if (cur->occ == 0 && (cur->size >= size + sizeof(struct Block)))
			{
				if (bestfit == NULL) // If we don't have a best fit, set it.
				{
					bestfit = cur;
				}
				else if (cur->size < bestfit->size) // If we already do have a best fit, compare it.
				{
					bestfit = cur;
					if (bestfit->size == size + sizeof(struct Block)) // If Best fit is the size that we need then we will just stop the search.
					{
						cur == NULL;
					}
				}
			}

			cur = cur->next;
		}
		if (bestfit != NULL) //If we found a Best Fit, we will use it!
		{
			int sizeNeeded = size + sizeof(struct Block);
			int sizeHas = bestfit->size;

			if (sizeHas - sizeNeeded > sizeof(struct Block)) // See if we can split the best fit into another block.
			{
				struct Block *split = (struct Block*) ((void*)bestfit + sizeNeeded);
				split->prev = bestfit;
				split->next = bestfit->next;
				split->next->prev = split;
				split->occ = 0;
				split->size = sizeHas - sizeNeeded;
				bestfit->size = sizeNeeded;
				bestfit->next = split;
			}


			bestfit->occ = 1;
			return (void *)((char*)bestfit + sizeof(struct Block));
		}
		struct Block *bloc = (struct Block*) sbrk(sizeof(struct Block)); //If we don't have a Best Fit, then we will just put the node at the end.
		bloc->occ = 1;
		bloc->size = size + sizeof(struct Block);
		bloc->prev = cur;
		cur->next = bloc;
	}
	return sbrk(size);
}

/** @brief Mark the block free by unsetting the occ bit
*
* In a real heap the freed block would be reused but
* this simple heap always allocats from the top of the
* heap by increasing heap space.
*
* @param data Pointer to area being freed by user.
* @return Void.
*/

void my_free(void *data) {
	struct Block *cur;
	struct Block *freed;

	for (cur = head; cur != NULL; cur = cur->next) //Look through the linked list for the node with the data.
	{
		if ((char*)cur <= (char*)data && (char*)data < (char*)cur + cur->size)
		{
			cur->occ = 0;
			freed = cur;
		}
	}

	if (freed->prev != NULL && freed->prev->occ == 0) //Check to see if we can coalesce with the previous node.
	{
		freed = freed->prev;
		freed->size = freed->size + freed->next->size;
		freed->next = freed->next->next;
		if (freed->next != NULL)
		{
			freed->next->prev = freed;
		}
	}


	if (freed->next != NULL && freed->next->occ == 0) // Check to see if we can coalesce with the next node.
	{
		freed->size = freed->size + freed->next->size;
		freed->next = freed->next->next;
		if (freed->next != NULL)
		{
			freed->next->prev = freed;
		}
	}

	if ((char*)freed + freed->size == sbrk(0)) // See if the node is at the end of the list, if so lets shrink the heap.
	{
		int size = freed->size;
		if (head != freed)
		{
			freed->prev->next = NULL;
		}
		else
		{
			head = NULL;
		}
		freed = NULL;
		sbrk((intptr_t)(size*(-1)));
	}
}

/** @brief Dump the contents of the heap.
*
* Traverse the heap starting from the head of the list and print
* each block.  While traversing, check the integrity of the heap
* through various assertions.
*
* @return Void.
*/
void dump_heap() {
	struct Block *cur;
	printf("brk: %p\n", sbrk(0));
	printf("head->");
	for (cur = head; cur != NULL; cur = cur->next)
	{
		printf("[%d:%d:%d]->", cur->occ, (char*)cur - (char*)head, cur->size);
		fflush(stdout);
		assert((char*)cur >= (char*)head && (char*)cur + cur->size <= (char*)sbrk(0)); // check that block is within bounds of the heap
		if (cur->next != NULL)
		{
			assert(cur->next->prev == cur); // if not last block, check that forward/backward links are consistent
			assert((char*)cur + cur->size == (char*)cur->next); // check that the block size is correctly set
		}
	}
	printf("NULL\n");
}
