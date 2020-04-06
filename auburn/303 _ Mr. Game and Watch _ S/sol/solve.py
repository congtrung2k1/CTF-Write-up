#use http://www.javadecompilers.com/ to decompile the java and jar, DO NOT use jdgui

#masterchief = md5_re(d5c67e2fc5f5f155dff8da4bdc914f41)


#sha1(inp) == dec(sec_2, key_2 = 64)
sec_2 = [114, 118, 116, 114, 113, 114, 36, 37, 38, 38, 120, 121, 33, 36, 37, 113, 117, 118, 118, 113, 33, 117, 121, 37, 119, 34, 118, 115, 114, 120, 119, 114, 36, 120, 117, 120, 38, 114, 35, 118]
print(''.join([chr(i ^ 64) for i in sec_2]))
#princesspeach = sha1_re()


#enc(sha-256(inp) , key_3 = 313) == sec_3
#s(inp) = de_enc(sec_3 , key_3)
sec_3 = [268, 348, 347, 347, 269, 256, 348, 269, 256, 256, 344, 271, 271, 264, 266, 348, 257, 266, 267, 348, 269, 266, 266, 344, 267, 270, 267, 267, 348, 349, 349, 265, 349, 267, 256, 269, 270, 349, 268, 271, 351, 349, 347, 269, 349, 271, 257, 269, 344, 351, 265, 351, 265, 271, 346, 271, 266, 264, 351, 349, 351, 271, 266, 266]
print(''.join([chr(i ^ 313) for i in sec_3]))
#solidsnake = sha-256_re()

#auctf{If_u_h8_JAVA_and_@SM_try_c_sharp_2922}
