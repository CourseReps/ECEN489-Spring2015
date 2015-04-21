# JSON Messages

**Action:** Add friends

*Client Message:* {"addFriends":{"friends":["Alice","Bob","Charlie"],"username":"Jim"}}

*Server Message:* {"outcome":"success"}

**Action:** Add new check in

*Client Message:* {"checkIn":{"method":"openCV","location":"EIC","time":1429112854192,"username":"Bob"}}

*Server Message:* {"outcome":"success"}

**Action:** Get last 5 friends at a location

*Client Message:* {"recentFriends":{"locations":["EIC","ZachShack"],"username":"Jim"}}

*Server Message:* {"recentFriends":[{"location":"EIC","friends":["Alice","Bob","Charlie","David","Elliot"]},"location":"ZachShack",...]}

**Action:** Get friend's last five locations

*Client Message:* {"recentLocs":{"friends":["Alice","Bob","Charlie"],"username":"Jim"}}

*Server Message:* {"friendsLocs":[{"locations":["EIC","ZachShack","Bright","MSC","Rudder"],"username":"Alice"},{"locations":["EIC","ZachShack","Bright","MSC","Rudder"],"username":"Bob"}, [...], "username":"Charlie"]}

**Action:** Get friends

*Client Message:* {"selectFriends":"Jim"}

*Server Message:* {"friends":[{"username":"Alice"},{"username":"Bob"}]}

