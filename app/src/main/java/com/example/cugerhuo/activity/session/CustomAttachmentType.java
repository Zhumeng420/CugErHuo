package com.example.cugerhuo.activity.session;

// 多端统一
public interface CustomAttachmentType {
    int Guess = 1;
    int SnapChat = 2;
    int Sticker = 3;
    int RTS = 4;
    int RedPacket = 5;
    int OpenedRedPacket = 6;
    int MyOrderCustomMsg = 10002;
    int ToBeConfirmedCustomMsg = 10003;

//这里需要注意。需要与后台设置统一。后台设置的时候。不是100的那个自定义消息类型，而是消息body中的type
}


