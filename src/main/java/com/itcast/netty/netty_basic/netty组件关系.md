##阅读源码 Channel ChannelHandler ChannelPipeline ChannelHandlerContext

>A ChannelHandler is provided with a ChannelHandlerContext object. 
> A ChannelHandler is supposed to interact with the ChannelPipeline it belongs to via a context object.
> Using the context object, the ChannelHandler can pass events upstream or downstream, 
> modify the pipeline dynamically, or store the information (using AttributeKeys) which is specific to the handler.

 
> Each channel has its own pipeline and it is created automatically when a new channel is created.


## ChannelHandlerContext
>Enables a ChannelHandler to interact with its ChannelPipeline and other handlers. Among other things a handler can notify the next ChannelHandler in the ChannelPipeline as well as modify the ChannelPipeline it belongs to dynamically.