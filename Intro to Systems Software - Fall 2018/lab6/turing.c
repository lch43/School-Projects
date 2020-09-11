#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>

int main()
{
	int sfd;
	struct sockaddr_in addr;
	int PORT_MIN = 51080;
	int PORT_MAX = 51099;
	int MYPORT = PORT_MIN - 1;

	sfd = socket(PF_INET, SOCK_STREAM, 0);
	if (sfd == -1)
	{
		printf("Socket failed");
		return -1;
	}
	do
	{	
		if (MYPORT + 1 <= PORT_MAX)
                {
                        MYPORT++;
                }
		memset(&addr, 0, sizeof(addr));
		addr.sin_family = AF_INET;
		addr.sin_port = htons(MYPORT);
		addr.sin_addr.s_addr = INADDR_ANY; //automatically find IP
	}
	while (bind(sfd, (struct sockaddr *)&addr, sizeof(addr)) == -1);

	if (listen(sfd, 10) == -1)
	{
		printf("Listen failed");
		return -1;
	}
	
	int connfd = accept(sfd, NULL, NULL);
	if (connfd == -1)
	{
		return -1;
	}

	char buffer[1024];
	strcpy(buffer, "Hello there!\n");
	send(connfd, buffer, strlen(buffer), 0);
	
	char response[3];
	if(recv(connfd, response, strlen(response), 0) == -1)
	{
		return -1;
	}


        strcpy(buffer, "It was nice, meeting you, but I need to go! See ya later!\n");
        send(connfd, buffer, strlen(buffer), 0);


	close(connfd);
	close(sfd);
}
