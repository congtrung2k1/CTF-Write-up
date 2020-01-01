# uncompyle6 version 3.6.1
# Python bytecode 2.7 (62211)
# Decompiled from: Python 3.7.4 (tags/v3.7.4:e09359112e, Jul  8 2019, 19:29:22) [MSC v.1916 32 bit (Intel)]
# Embedded file name: emco.py
from tkinter import *
import tkMessageBox
from tkFileDialog import *
import os, sys
from math import *
from PIL import ImageTk, Image
from ttk import *

def resource_path(relative_path):
    try:
        base_path = sys._MEIPASS
    except Exception:
        base_path = os.path.abspath('.')

    return os.path.join(base_path, relative_path)


def convert_size(size_bytes):
    if size_bytes == 0:
        return '0B'
    size_name = ('B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB')
    i = int(floor(log(size_bytes, 1024)))
    p = pow(1024, i)
    s = round(size_bytes / p, 2)
    return '%s %s' % (s, size_name[i])


def file_size(file_path):
    if os.path.isfile(file_path):
        file_info = os.stat(file_path)
        return convert_size(file_info.st_size)


def updateSizeLabel(file_path):
    size_text.set('Original Size : ' + file_size(file_path))


def select(panel):
    global file_path
    chk_path = askopenfilename(title='Select file', filetypes=(('png files', '*.png'), ('jpeg files', '*.jpg')))
    img = ImageTk.PhotoImage(Image.open(chk_path))
    if img.height() != 200 or img.width() != 200:
        tkMessageBox.showerror('Status', 'Please enter a valid B/W image of dimensions 200x200')
        return
    file_path = chk_path
    panel.configure(image=img)
    panel.image = img
    updateSizeLabel(file_path)


def encrypt(panel):
    im = Image.open(file_path).convert('RGB')
    width, height = im.size
    pixels = list(im.getdata())
    pixels = [ pixels[i * width:(i + 1) * width] for i in range(height) ]
    binary_pixels = []
    for item in pixels:
        for pixel in item:
            if pixel == (255, 255, 255):
                binary_pixels.append('0')
            else:
                binary_pixels.append('1')

    line = ('').join(binary_pixels)
    n = 8
    enc = [ int(line[i:i + n], 2) for i in range(0, len(line), n) ]
    data = ''
    enc_len = len(enc)
    for i in range(enc_len):
        data += chr(125) + chr(0) + chr(enc[i])

    s = int(sqrt(enc_len))
    im2 = Image.frombytes('RGB', (s, s), data)
    im2.save('encrypted.png', 'PNG')
    encimg = ImageTk.PhotoImage(im2)
    panel.configure(image=encimg)
    panel.img = encimg
    size_text.set('Compressed Size : ' + file_size('encrypted.png'))
    tkMessageBox.showinfo('Status', 'Encrypted Image saved at current location')


root = Tk()
root.title("MrT4ntr4's EmCo App")
root.geometry('270x270')
icon_path = resource_path('images\\lockicon2.ico')
root.iconbitmap(icon_path)
root.style = Style()
root.style.theme_use('clam')
bottom = Frame(root)
bottom.pack(side='bottom')
splashimg_path = resource_path('images\\splash.png').replace('\\', '\\\\')
ph = Image.open(splashimg_path)
img = ImageTk.PhotoImage(ph)
panel = Label(root, image=img)
panel.image = img
panel.pack(side='top', expand=True)
size_text = StringVar()
sizel = Label(root, textvariable=size_text)
sizel.configure(anchor='center')
size_text.set('Select a 200x200 B/W Image to begin')
sizel.pack(ipadx=10, ipady=10)
prev_button = Button(root, text='Select', command=lambda : select(panel))
prev_button.pack(in_=bottom, side='left')
next_button = Button(root, text='Encrypt', command=lambda : encrypt(panel))
next_button.pack(in_=bottom, side='right')
root.mainloop()