void my_strcpy(char *dest, char *src)
{
	while(*dest++ = *src++);
}

/*void my_strcat(char *dest, char *src)
{
	int i = 0;
	int j = 0;
	while(dest[i] >= 32 && dest[i] <= 127)
	{
	i++;
	}
	while(src[j] >= 32 && src[j] <= 126)
        {
	dest[i+j] = src[j];
        j++;
        }
}*/

void my_strcat(char *dest, char *src)
{
        int i,j;
    for (i = 0; dest[i] != '\0'; i++);
    for (j = 0; src[j] != '\0'; j++)
        dest[i+j] = src[j];
    dest[i+j] = '\0';
}
