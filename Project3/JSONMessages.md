# JSON Messages

**Action:** Add friends

*Client Message:* {"addFriends":{"friends":["Alice","Bob","Charlie"],"username":"Jim"}}

*Server Message:* {"outcome":"success"}

**Action:** Add new check in

*Client Message:* {"checkIn":{"method":"openCV","location":"EIC","time":1429112854192,"userName":"Bob"}}

*Server Message:* {"outcome":"success"}

**Action:** Get last 5 friends at a location

*Client Message:* {"recentFriends":{"locations":["EIC","ZachShack"],"username":"Jim"}}

*Server Message:* {"recentFriends":[{"location":"EIC","friends":{"friend0":"Bob", ... ,"friend4":"Alice"}},"location":"ZachShack",...]}

**Action:** Get friend's last five locations

*Client Message:* {"recentLocs":{"friends":["Alice","Bob","Charlie"],"username":"Jim"}}

*Server Message:* {"friendsLocs":[{"locations":{"location0":"EIC",...,"location4":"ZachShack"},"username":"Alice"},{"locations":{"location0":"Bright",...,"location4":"EIC"},"username":"Bob"}, {...}, "username":"Charlie"]}

**Action:** Get friends

*Client Message:* {"selectFriends":"Jim"}

*Server Message:* {"friends":[{"username":"Alice"},{"username":"Bob"}]}

