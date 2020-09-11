#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>

struct Node
{
	int grade;
	struct Node *next;
};

struct Node* createNode(int grade1)
{
	int input;
	struct Node* pHead = (struct Node*)malloc(sizeof(struct Node));
	struct Node* pTail = pHead;
	pHead->grade = grade1;
	pHead->next = NULL;
	while (1)
	{
		scanf("%d", &input);
		if (input < 0)
			break;
		struct Node* next = (struct Node*)malloc(sizeof(struct Node));
		pTail->next=next;
		pTail = next;
		pTail->grade = input;
		pTail->next = NULL;
	}
	return pHead;
}

void traverseAvg(struct Node* list)
{
	int sum = 0;
	int num = 0;
	struct Node* current = list;
	while (current != NULL)
	{
		sum = sum + current->grade;
		num++;
		current = current->next;
	}
	printf("The average grade is: %d\n", sum / num);
}

void cleanUp(struct Node* list)
{
	while (list->next != NULL)
	{
		struct Node* secondLast = list;
		struct Node* last;
		while (secondLast->next->next != NULL)
		{
			secondLast = secondLast->next;
		}
		last = secondLast->next;
		free(last);
		secondLast->next = NULL;
	}
	free(list);
}

void main()
{
	int enter;
	printf("Enter a grade or enter -1 to stop: ");
	scanf("%d", &enter);
	if (enter>-1)
	{
		struct Node* list = createNode(enter);
		traverseAvg(list);
		//cleanUp(list);
	}
}
