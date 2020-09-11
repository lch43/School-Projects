#Landon Higinbotham
#lch43
#This project does have sound at the end when the player wins or loses, please turn on volume for full effect.
.data

# LED colors (don't change)
.eqv	LED_OFF		0
.eqv	LED_RED		1
.eqv	LED_ORANGE	2
.eqv	LED_YELLOW	3
.eqv	LED_GREEN	4
.eqv	LED_BLUE		5
.eqv	LED_MAGENTA	6
.eqv	LED_WHITE	7

# Board size (don't change)
.eqv	LED_SIZE		2
.eqv	LED_WIDTH	32
.eqv	LED_HEIGHT	32

# System Calls
.eqv	SYS_PRINT_INTEGER	1
.eqv	SYS_PRINT_STRING		4
.eqv	SYS_PRINT_CHARACTER	11
.eqv	SYS_SYSTEM_TIME		30

# Key states
leftPressed:		.word	0
rightPressed:		.word	0
upPressed:		.word	0
downPressed:		.word	0
actionPressed:		.word	0

# Frame counting
lastTime:		.word	0
frameCounter:		.word	0


millipede:		.byte '0':20
millipedeSize:		.word 10
millipedeDir:		.word 0 #0 for heading right, 1 for heading left

playerX:			.word 16
playerY:			.word 31

projectileX:		.word 0
projectileY:		.word 0
projectileActive:	.word 0

loopCount:		.word 0

gameExit:		.word 0 #if 1 then game over

.text
.globl main
main:	
	# Initialize the game state
	jal	initialize				# initialize()
	
	# Run our game!
	jal	gameLoop				# gameLoop()
	
	# The game is over.
	lw t0, millipedeSize
	beq t0, 0, winnerJump
	jal loser
	j endGame
winnerJump:
	jal winner
endGame:
	# Exit
	li	v0, 10
	syscall						# syscall(EXIT)

# void initialize()
#   Initializes the game state.
initialize:
	push	ra

	# Set lastTime to a reasonable number
	jal	getSystemTime				#
	sw	v0, lastTime
	
	# Clear the screen
	li	a0, 1
	jal	displayRedraw				# displayRedraw(1);
	
	# Initialize anything else
	
	jal initializeMillipede
	jal initializePlayer
	jal initializeMushrooms
	jal getSystemTime
	sw v0, loopCount
	
	pop	ra
	jr	ra
	
	#------------------------------------------------------------------------

initializeMillipede:
	push ra
	lw t0, millipedeSize
	la t1, millipede
	li t3, 0
	li t4, 0
initialize_Loop:
	blt t0, 0, initialize_Exit
	mul t2, t0, 2
	sub t2, t2, 1
	add t2, t2, t1 #puts address into t2 for y
	sb t3, (t2)
	sub t2, t2, 1 #Gets address for x
	sb t4, (t2)
	add t4, t4, 1
	sub t0, t0, 1
	j initialize_Loop
initialize_Exit:
	j refreshMillipede

shiftMillipede: #Loop that controls the movement of the millipede
	push ra
	la t0, millipede
	lw t1, millipedeSize
	bne t1, 0, continue_shiftMillipede
	li t0, 1
	sw t0, gameExit
	j endSkip
continue_shiftMillipede:
	mul t3, t1, 2
	add t3, t3, t0
	sub t3, t3, 1
	lb a1, (t3)
	sub t3, t3, 1
	lb a0, (t3)
	li a2, 0
	jal displaySetLED
	beq t1, 1, shiftHead
shift_subtractOne:
	subi t1, t1, 1
shiftMillipede_Loop:
	la t0, millipede
	beq t1, 0, shiftHead
	mul t2, t1, 2
	sub t2, t2, 1
	add t2, t2, t0  #Find the address of the value that we want to move the Y value to.
	addi t3, t2, 2 #Find the destination that we want to move the Y value to.
	lb t4, (t2)
	sb t4, (t3)
	mul t2, t1, 2
	sub t2, t2, 2
	add t2, t2, t0  #Find the address of the value that we want to move the X value to.
	addi t3, t2, 2 #Find the destination that we want to move the X value to.
	lb t4, (t2)
	sb t4, (t3)
	j shift_subtractOne
shiftHead:
	lw t0, millipedeDir
	beq t0, 0, shiftHead_Right
shiftHead_Left:
	la t0, millipede
	lb t1, (t0)
	beq t1, 0, moveDown_Left
	sub a0, t1, 1
	add t3, t0, 1
	lb a1, (t3)
	jal displayGetLED
	beq v0, 4, moveDown_Left
	sub t1, t1, 1
	sb t1, (t0)
	j shiftHead_Cont
moveDown_Left:
	la t0, millipede
	li t1, 0
	sw t1, millipedeDir
	add t0, t0, 1
	lb t2, (t0)
	add t1, t2, 1
	sb t1, (t0)
	lb t2, (t0)
	beq t2, 32, shiftHead_toTheTop
	j shiftHead_Cont
shiftHead_toTheTop:
	la t0, millipede
	li t1, 0
	sb t1, (t0)
	add t0, t0, 1
	sb t1, (t0)
	j shiftHead_Cont
shiftHead_Right:
	la t0, millipede
	lb t1, (t0)
	beq t1, 31, moveDown_Right
	add a0, t1, 1
	add t3, t0, 1
	lb a1, (t3)
	jal displayGetLED
	beq v0, 4, moveDown_Right
	add t1, t1, 1
	sb t1, (t0)
	j shiftHead_Cont
moveDown_Right:
	la t0, millipede
	li t1, 1
	sw t1, millipedeDir
	add t0, t0, 1
	lb t2, (t0)
	add t1, t2, 1
	sb t1, (t0)
	j shiftHead_Cont
shiftHead_Cont:
	la t0, millipede
	lb t1, (t0)
	add t0, t0, 1
	lb t2, (t0)
	lw t3, playerX
	lw t4, playerY
	bne t1, t3, shiftHead_Cont_Cont
	bne t2, t4, shiftHead_Cont_Cont
	li t0, 1
	sw t0, gameExit
shiftHead_Cont_Cont:
	j refreshMillipede
refreshMillipede:
	push s0
	lw s0, millipedeSize
refresh_Loop:
	la t0, millipede
	mul t1, s0, 2
	sub t1, t1, 1
	add t1, t1, t0
	lb a1, (t1)
	mul t1, s0, 2
	sub t1, t1, 2
	add t1, t1, t0
	lb a0, (t1)
	li a2, 1
	jal displaySetLED
	sub s0, s0, 1
	bgt s0, 0, refresh_Loop
endSkip:
	pop s0
	pop ra
	jr ra

initializePlayer: #Places the player for the current frame
	push ra
	lw a0, playerX
	lw a1, playerY
	li a2, 5
	jal displaySetLED
	pop ra
	jr ra

uninitializePlayer: #Deletes where the player was in the previous frame
	push ra
	lw a0, playerX
	lw a1, playerY
	li a2, 0
	jal displaySetLED
	pop ra
	jr ra

initializeMushrooms: #A loop to make the starting mushrooms
	push ra
	push s0
	li s0, 45 #Have 45 chances at creating mushrooms
initializeMushroomLoop:
	jal createMushroom
	li a0, 0
	jal displayRedraw #I added this, because I had a problem where the game would for some reason clear the screen when the game loop stars, even when a0 = 0
	sub s0, s0, 1
	bgt s0, 0, initializeMushroomLoop
	pop s0
	pop ra
	jr ra

createMushroom: #Creates a mushroom in a random location that is free.
	push ra
	push s0
	push s1
	jal randomBound
	move s0, a0
	jal randomBound
	move s1, a0
	jal placeMushroom
	pop s1
	pop s0
	pop ra
	jr ra
placeMushroom:
	push ra
	move a0, s0
	move a1, s1
	jal displayGetLED
	beq v0, 0, setMushroomColor
	pop ra
	jr ra
setMushroomColor:
	move a0, s0
	move a1, s1
	li a2, 4
	jal displaySetLED
	pop ra
	jr ra
	
randomBound:
	push ra
	li a0, 0
	li a1, 32
	li v0, 42
	syscall
	pop ra
	jr ra

updateProjectile:
	push ra
	lw t0, projectileY
	bne t0, 0, updateProjectile_Cont_Cont
	li t0, 0
	sw t0, projectileActive
	lw a0, projectileX
	lw a1, projectileY
	li a2, 0
	jal displaySetLED
	li t0, -1
	sw t0, projectileX
	sw t0, projectileY
updateProjectile_Cont_Cont:
	lw t0, projectileActive
	beq t0, 0, updateProjectile_Cont
	lw a0, projectileX
	lw a1, projectileY
	jal displayGetLED
	bne v0, 7, updateProjectile_Skipped #Skips over the lines that clears the projectile, if the projectile has already been overwritten by another block
	lw a0, projectileX
	lw a1, projectileY
	li a2, 0
	jal displaySetLED

updateProjectile_Skipped:
	lw a0, projectileX
	lw a1, projectileY
	sub a1, a1, 1
	jal displayGetLED
	beq v0, 1, updateProjectile_HitSnake
	beq v0, 4, updateProjectile_HitMushroom
	lw a0, projectileX
	lw a1, projectileY
	sub a1, a1, 1
	li a2, 7
	sw a1, projectileY
	jal displaySetLED
	j updateProjectile_Cont
updateProjectile_HitSnake:
	la t0, millipede
	lw t1, millipedeSize
	sub t1, t1, 1
	mul t1, t1, 2
	add t2, t1, 1
	add t1, t0, t1
	add t2, t0, t2
	lb a0, (t1)
	lb a1, (t2)
	li a2, 0
	jal displaySetLED
	li t0, 0
	sw t0, projectileActive
	lw t0, millipedeSize
	sub t0, t0, 1
	sw t0, millipedeSize
	j updateProjectile_Cont
updateProjectile_HitMushroom:
	li t0, 0
	sw t0, projectileActive
	lw a0, projectileX
	lw a1, projectileY
	sub a1, a1, 1
	li a2, 0
	jal displaySetLED
updateProjectile_Cont:
	pop ra
	jr ra
	
winner:#Shows "YOU WIN" and plays sounds to make you feel good.
	push ra
	li a0, 3
	jal you
	li a0, 0
	jal displayRedraw
	li a0, 55
	li a1, 200
	li a2, 57
	li a3, 127
	li v0, 33
	syscall
	li a0, 59
	li a1, 200
	li a2, 57
	li a3, 127
	li v0, 33
	syscall
	li a0, 62
	li a1, 200
	li a2, 57
	li a3, 127
	li v0, 33
	syscall
	li a0, 3
	jal win
	li a0, 0
	jal displayRedraw
	li a0, 67
	li a1, 2000
	li a2, 57
	li a3, 127
	li v0, 33
	syscall
	pop ra
	jr ra
loser: #Shows "YOU LOSE" and plays music to make you feel bad
	push ra
	li a0, 2
	jal you
	li a0, 0
	jal displayRedraw
	li a0, 38
	li a1, 500
	li a2, 33
	li a3, 127
	li v0, 33
	syscall
	li a0, 2
	jal lose
	li a0, 0
	jal displayRedraw
	li a0, 31
	li a1, 2000
	li a2, 33
	li a3, 127
	li v0, 33
	syscall
	pop ra
	jr ra
				
# void gameLoop()
#   Infinite loop for the game logic
gameLoop:
	push	ra

gameLoopStart:						# loop {
	jal	getSystemTime				#
	move	s0, v0					# 	s0 = getSystemTime();
	
	move	a0, s0
	jal	handleInput				# 	v0 = handleInput(elapsed: a0);
	
	# Determine if a frame passed
	lw	t0, lastTime
	sub	t0, s0, t0
	blt	t0, 50, gameLoopStart			# 	if (s0 - lastTime >= 50) {
	
	# Update last time
	sw	s0, lastTime				# 		lastTime = s0;
	
	# Update our game state (if a frame elapsed)
	move	a0, s0
	jal	update					# 		v0 = update();
	
mushroomWaitCheck:	
	jal getSystemTime
	lw t0, loopCount
	move t1, v0
	sub t2, t1, t0
	blt t2, 1000, gameLoopStart_Continue
	sw t1, loopCount
	jal createMushroom
	
gameLoopStart_Continue:
	jal updateProjectile #Moves the projectile
	jal shiftMillipede #Moves the centipede
	
	# Exit the game when it tells us to
	lw t0, gameExit
	beq	t0, 1, gameLoopExit			# 		if (t0 == 1) { break; }
	
	# Redraw (a0 = 0; do not clear the screen!)
	li	a0, 0
	jal	displayRedraw				# 		displayRedraw(0);
							#	}
	j	gameLoopStart				# }

gameLoopExit:
	pop	ra
	jr	ra					# return;
			
# int getSystemTime()
#   Returns the number of milliseconds since system booted.
getSystemTime:
	# Now, get the current time
	li	v0, SYS_SYSTEM_TIME
	syscall						# a0 = syscall(GET_SYSTEM_TIME);
	
	move	v0, a0
	
	jr	ra					# return v0;
	
# bool update(elapsed)
#   Updates the game for this frame.
update:
	push	ra
	push	s0
	
	# Increment the frame counter
	lw	t0, frameCounter
	add	t0, t0, 1
	sw	t0, frameCounter			# frameCounter++;
	
	li	s0, 0					# s0 = 0;
	
	# Update all of the game state
	jal	updateStuff
	or	s0, s0, v0				# s0 = s0 | updateStuff();
	
_updateExit:
	move	v0, s0
	
	pop	s0
	pop	ra
	jr	ra					# return s0;
	
# void updateStuff()
updateStuff:
	push	ra
	push	s0
	
	jal	uninitializePlayer
	
_updateStuffLeft:#Updates the player's X coordinate if the player is holding the left arrow
	lw	t0, leftPressed
	beq	t0, 0, _updateStuffRight
	
	
	lw	s0, playerX
	beq	s0, 0, _updateStuffRight
	sub	s0, s0, 1
	
	move	a0, s0
	lw	a1, playerY
	jal	displayGetLED
	bne	v0, 0, _updateStuffRight
	
	sw	s0, playerX

_updateStuffRight:#Updates the player's X coordinate if the player is holding the right arrow
	lw	t0, rightPressed
	beq	t0, 0, _updateStuffUp
	
	lw	s0, playerX
	beq	s0, 31, _updateStuffUp
	add	s0, s0, 1
	
	move	a0, s0
	lw	a1, playerY
	jal	displayGetLED
	bne	v0, 0, _updateStuffUp
	
	sw	s0, playerX

_updateStuffUp:#Updates the player's Y coordinate if the player is holding the up arrow
	lw	t0, upPressed
	beq	t0, 0, _updateStuffDown
	
	lw	s0, playerY
	beq	s0, 0, _updateStuffDown
	sub	s0, s0, 1
	
	move	a1, s0
	lw	a0, playerX
	jal	displayGetLED
	bne	v0, 0, _updateStuffDown
	
	sw	s0, playerY

_updateStuffDown: #Updates the player's Y coordinate if the player is holding the down arrow
	lw	t0, downPressed
	beq	t0, 0, _updateStuffAction
	
	lw	s0, playerY
	beq	s0, 31, _updateStuffAction
	add	s0, s0, 1
	
	move	a1, s0
	lw	a0, playerX
	jal	displayGetLED
	bne	v0, 0, _updateStuffAction
	
	sw	s0, playerY

_updateStuffAction: #Sees if the player is pressing 'B' and if there is already a projectile in the air. If there is no projectile and the player is holding it fires one
	lw	t0, actionPressed
	beq	t0, 0, _updateStuffExit
	lw	t0, projectileActive
	beq	t0, 1, _updateStuffExit
	
	li	t0, 1
	sw	t0, projectileActive
	
	lw	t0, playerX
	sw	t0, projectileX
	
	lw	t0, playerY
	sw	t0, projectileY
	

_updateStuffExit:

	jal initializePlayer
	
	pop	s0
	pop	ra
	jr	ra					# return 0;
	
# LED Input Handling Function
# -----------------------------------------------------
	
# bool handleInput(elapsed)
#   Handles any button input.
# returns: v0: 1 when the game should end.
handleInput:
	push	ra
	
	# Get the key state memory
	li	t0, 0xffff0004
	lw	t1, (t0)
	
	# Check for key states
	and	t2, t1, 0x1
	sw	t2, upPressed
	
	srl	t1, t1, 1
	and	t2, t1, 0x1
	sw	t2, downPressed
	
	srl	t1, t1, 1
	and	t2, t1, 0x1
	sw	t2, leftPressed
	
	srl	t1, t1, 1
	and	t2, t1, 0x1
	sw	t2, rightPressed
	
	srl	t1, t1, 1
	and	t2, t1, 0x1
	sw	t2, actionPressed
	
	move	v0, t2
	
	pop	ra
	jr	ra
	
# LED Display Functions
# -----------------------------------------------------
	
# void displayRedraw()
#   Tells the LED screen to refresh.
#
# arguments: $a0: when non-zero, clear the screen
# trashes:   $t0-$t1
# returns:   none
displayRedraw:
	li	t0, 0xffff0000
	sw	a0, (t0)
	jr	ra

# void displaySetLED(int x, int y, int color)
#   sets the LED at (x,y) to color
#   color: 0=off, 1=red, 2=yellow, 3=green
#
# arguments: $a0 is x, $a1 is y, $a2 is color
# returns:   none
#
displaySetLED:
	push	s0
	push	s1
	push	s2
	
	# I am trying not to use t registers to avoid
	#   the common mistakes students make by mistaking them
	#   as saved.
	
	#   :)

	# Byte offset into display = y * 16 bytes + (x / 4)
	sll	s0, a1, 6      # y * 64 bytes
	
	# Take LED size into account
	mul	s0, s0, LED_SIZE
	mul	s1, a0, LED_SIZE
		
	# Add the requested X to the position
	add	s0, s0, s1
	
	li	s1, 0xffff0008 # base address of LED display
	add	s0, s1, s0    # address of byte with the LED
	
	# s0 is the memory address of the first pixel
	# s1 is the memory address of the last pixel in a row
	# s2 is the current Y position	
	
	li	s2, 0	
_displaySetLEDYLoop:
	# Get last address
	add	s1, s0, LED_SIZE
	
_displaySetLEDXLoop:
	# Set the pixel at this position
	sb	a2, (s0)
	
	# Go to next pixel
	add	s0, s0, 1
	
	beq	s0, s1, _displaySetLEDXLoopExit
	j	_displaySetLEDXLoop
	
_displaySetLEDXLoopExit:
	# Reset to the beginning of this block
	sub	s0, s0, LED_SIZE
	
	# Move to next row
	add	s0, s0, 64
	
	add	s2, s2, 1
	beq	s2, LED_SIZE, _displaySetLEDYLoopExit
	
	j _displaySetLEDYLoop
	
_displaySetLEDYLoopExit:
	
	pop	s2
	pop	s1
	pop	s0
	jr	ra
	
# int displayGetLED(int x, int y)
#   returns the color value of the LED at position (x,y)
#
#  arguments: $a0 holds x, $a1 holds y
#  returns:   $v0 holds the color value of the LED (0 through 7)
#
displayGetLED:
	push	s0
	push	s1

	# Byte offset into display = y * 16 bytes + (x / 4)
	sll	s0, a1, 6      # y * 64 bytes
	
	# Take LED size into account
	mul	s0, s0, LED_SIZE
	mul	s1, a0, LED_SIZE
		
	# Add the requested X to the position
	add	s0, s0, s1
	
	li	s1, 0xffff0008 # base address of LED display
	add	s0, s1, s0    # address of byte with the LED
	lbu	v0, (s0)
	
	pop	s1
	pop	s0
	jr	ra
	
#My LED text begins under here.

you:	#Takes a0 as the color you want to set the word YOU to.
	push ra
	push s0
	move s0, a0

	li a0, 15 #X
	li a1, 14 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 15 #X
	li a1, 10 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 10 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 11 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 12 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 13 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 14 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 10 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 11 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 12 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 13 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 14 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 10 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 11 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 12 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 13 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 14 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 19 #X
	li a1, 14 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 10 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 11 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 12 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 13 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 14 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 12 #X
	li a1, 10 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 12 #X
	li a1, 11 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 11 #X
	li a1, 12 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 11 #X
	li a1, 13 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 11 #X
	li a1, 14 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 10 #X
	li a1, 10 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 10 #X
	li a1, 11 #Y
	move a2, s0 #Color of word
	jal displaySetLED
	
	pop s0
	pop ra
	jr ra
	
win:
	push ra
	push s0
	move s0, a0
	
	li a0, 10 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 19 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 12 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 21 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 21 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 21 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 21 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 21 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 17 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 17 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 17 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 17 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 17 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 9 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 9 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 9 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 9 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 9 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 11 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 11 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 11 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 11 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 11 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 13 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 13 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 13 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 13 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 13 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 15 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 15 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 15 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 15 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 15 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED
	
	
	pop s0
	pop ra
	jr ra

lose:	#Takes a0 as the color you want to set the word YOU to.
	push ra
	push s0
	move s0, a0

	li a0, 8 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 8 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 8 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 8 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 8 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 9 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 10 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 12 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 12 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 12 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 12 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 12 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 13 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 13 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 14 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 16 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 17 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 17 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 17 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 18 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 18 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 20 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 20 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 21 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 21 #X
	li a1, 19 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 21 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 22 #X
	li a1, 17 #Y
	move a2, s0 #Color of word
	jal displaySetLED

	li a0, 22 #X
	li a1, 21 #Y
	move a2, s0 #Color of word
	jal displaySetLED
	
	pop s0
	pop ra
	jr ra
