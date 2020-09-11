/*
 * /dev/dice skeleton code
 *
 * Wonsun Ahn <wahn@pitt.edu>
 *
 * Edited by Landon Higinbotham for Project 4
 */

#include <linux/fs.h>
#include <linux/init.h>
#include <linux/miscdevice.h>
#include <linux/module.h>
#include <linux/random.h>
#include <asm/uaccess.h>

int mode = 6;

unsigned char get_random_byte(int max) {
	unsigned char c;
	get_random_bytes(&c, 1);
	return c%max;
}

/*
 * dice_read is the function called when a process calls read() on
 * /dev/dice.  It writes count bytes to the buffer passed in the
 * read() call.
 */

static ssize_t dice_read(struct file * file, char * buf,
			  size_t count, loff_t *ppos)
{
        //printk(KERN_ERR "read(file, 0x%p, %d, %lld)\n", buf, count, *ppos);
	if(count == 0)
	{
		return 0;
	}
	else
	{
		int i = 0;
	        for(i; i < count; i++)
	        {
				unsigned char temp[1];
				temp[0] = (get_random_byte(mode) + 1);
				if (copy_to_user(buf + i, temp, 1) != 0)
				{
					return -EFAULT;
				} 
		}
		*ppos = *ppos + count;
		return count;
	}
}

/*
 * dice_write is the function called when a process calls write() on
 * /dev/dice.  It reads count bytes from the buffer passed in the
 * write() call.
 */

static ssize_t dice_write(struct file * file, const char * buf,
			  size_t count, loff_t *ppos)
{
        //printk(KERN_ERR "write(file, 0x%p, %d, %lld)\n", buf, count, *ppos);
        if(count == 0)
        {
                return 0;
        }
        else
        {
	        int i = 0;
		char modeHolder[1];
	        for(i; i < count; i++)
	        {
			if (copy_from_user(modeHolder, buf + i, 1) != 0)
			{
				return -EFAULT;
			}
	        }
		mode = modeHolder[0];
	        *ppos = *ppos + count;
	        return count;
	}
}

/*
 * Now we have two file operations: read, write
 */

static const struct file_operations dice_fops = {
	.owner		= THIS_MODULE,
	.read		= dice_read,
        .write          = dice_write,
};

static struct miscdevice dice_dev = {
	/*
	 * We don't care what minor number we end up with, so tell the
	 * kernel to just pick one.
	 */
	MISC_DYNAMIC_MINOR,
	/*
	 * Name ourselves /dev/dice.
	 */
	"dice",
	/*
	 * What functions to call when a program performs file
	 * operations on the device.
	 */
	&dice_fops
};

static int __init
dice_init(void)
{
	int ret;

	/*
	 * Create the "dice" device in the /sys/class/misc directory.
	 * Udev will automatically create the /dev/dice device using
	 * the default rules.
	 */
	ret = misc_register(&dice_dev);
	if (ret)
                printk(KERN_ERR "Unable to register dice misc device\n");
        else {
                printk(KERN_ERR "Registered device under <MAJOR , MINOR><%d %d>\n" , 10, dice_dev.minor);
                printk(KERN_ERR "Create device file using: mknod /dev/dice c %d %d\n", 10, dice_dev.minor);
        }

	return ret;
}

module_init(dice_init);

static void __exit
dice_exit(void)
{
	misc_deregister(&dice_dev);
}

module_exit(dice_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Wonsun Ahn <wahn@pitt.edu> and Landon Higinbotham");
MODULE_DESCRIPTION("/dev/dice skeleton code");
MODULE_VERSION("dev");
