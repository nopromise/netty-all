## ByteBuffer

### capacity、limit、position
> capacity是buffer元素的容量，不为负，不变；
> limit是第一个不可读或者不可写的元素的index； 读写的限制
> position是下一个可读或者可写的index；读写的指针


1. A buffer's capacity is the number of elements it contains. 
   The capacity of a buffer is never negative and never changes.
2. A buffer's limit is the index of the first element 
   that should not be read or written. 
   A buffer's limit is never negative and is never greater than its capacity.
3. A buffer's position is the index of the next element 
   to be read or written. A buffer's position is never negative 
   and is never greater than its limit.
4. The following invariant holds for the mark, position, limit, and capacity values:
   0 <= mark <= position <= limit <= capacity
5. A newly-created buffer always has a position of zero and a mark that is undefined. 
   The initial limit may be zero, 
   or it may be some other value that depends upon the type of 
   the buffer and the manner in which it is constructed. 
   Each element of a newly-allocated buffer is initialized to zero.   

### Clearing, flipping, and rewinding
1. clear makes a buffer ready for a new sequence of channel-read 
   or relative put operations: 
   It sets the limit to the capacity and the position to zero.
2. flip makes a buffer ready for a new sequence of channel-write 
   or relative get operations: 
   It sets the limit to the current position and then sets the position to zero.
3. rewind makes a buffer ready for re-reading the data 
   that it already contains: 
   It leaves the limit unchanged and sets the position to zero.