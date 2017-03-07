#ifndef __HELLWORLD_H__
#define __HELLWORLD_H__

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

#endif /* __HELLWORLD_H__ */
