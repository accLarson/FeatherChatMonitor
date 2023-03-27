# FeatherChatMonitor

FeatherChatMonitor is a paper plugin for minecraft servers.  
This plugin prevents spam, pings players when their name is said, and allows players to send distinguished messages.

The anti-spam system has a configurable warning for spam; if the spam continues, the player will be kicked.  
When a player's name is said in chat the player will hear a customizable *ping* notification.  
When a player sends a message beginning with `<distinguish>` their message will be colored as configured.  

### Dependencies:
This plugin depends on [EssentialsX](https://essentialsx.net/downloads.html) for detecting who should not be able to ping another player based on their ignore list.

### Permission Nodes:
    feather.chatmonitor.distinguish.<distinguish-group>   -  Allows the use of <distinguish>.
    feather.chatmonitor.spam.bypass                       -  Bypass the spam filter.

