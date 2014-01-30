schat
=====

A little Java app, exam-related, it aims to enstabilish an encrypted connection between two peers to enable a secure chat.

usage
=====

    $ java schat/Main
    first parameter can be 'autotest' to autocreate two peers that send each other an encrypted message.
    or can be a port to listen from, or an hostname:port to connect to


Autotest:

    $ java schat/Main autotest
    Server socket created: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=54200]
    Server socket created: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=54300]
    Server: connection accepted: Socket[addr=/127.0.0.1,port=37707,localport=54200]
    Client socket created: Socket[addr=redbaron/127.0.1.1,port=54200,localport=37707]
    PEER#54300: Client made a new connection!
    PEER#54200: Server received a new connection!
    PEER#54200: new connection established!
    PEER#54300: new connection established!
    PEER#54200: Exchanged a 8B key
    PEER#54200: Secure channel active
    PEER#54300: Exchanged a 8B key
    PEER#54300: Secure channel active
    Received encrypted: test two_to_one
    Received encrypted: test one_to_two

A real interactive session:

Term#1:

    $ java schat/Main 12345
    Server socket created: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=12345]
    Server: connection accepted: Socket[addr=/127.0.0.1,port=43615,localport=12345]
    PEER#12345: Server received a new connection!
    PEER#12345: new connection established!
    PEER#12345: Exchanged a 8B key
    PEER#12345: Secure channel active
    Received encrypted: message from client
    ...and from server

Term#2:

    $ java schat/Main localhost:12345
    Client socket created: Socket[addr=localhost/127.0.0.1,port=12345,localport=43615]
    PEER#54321: Client made a new connection!
    PEER#54321: new connection established!
    PEER#54321: Exchanged a 8B key
    PEER#54321: Secure channel active
    message from client
    Received encrypted: ...and from server

