#ifndef __BUFT_H__
#define __BUFT_H__

class BufferTrance 
{ 
private: 
    char* savebyte;
public: 
    BufferTrance (); 
    virtual ~BufferTrance ();
    
    int setSavebyte(char* inbyte);
    char* getSavebyte();
    /*public member*/
}; 

#endif /* __BUFT_H__ */
