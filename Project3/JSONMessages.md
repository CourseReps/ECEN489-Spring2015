# JSON Messages

**Action:** Login

*Client Message:* {"login":{"username":"Jim", "password":"pass"}}

*Server Message:* {"loginOutcome":{"outcome":"success", "sessionID":"hashedID"}}

or 

*Server Message:* {"loginOutcome":{"outcome":"failure", "sessionID":"null"}}

**Action:** Logout

*Client Message:* {"logout":{"username":"Jim","sessionID":"hashedID"}}

*Server Message:* No server response

**Action:** Add friends

*Client Message:* {"addFriends":{"friends":["Alice","Bob","Charlie"],"username":"Jim","sessionID":"hashedID"}}

*Server Message:* {"outcome":"success"}

**Action:** Add new check in

*Client Message:* {"checkIn":{"method":"openCV","location":"EIC","time":1429112854192,"username":"Bob","sessionID":"hashedID"}}

*Server Message:* {"outcome":"success"}

**Action:** Get last 5 friends at a location

*Client Message:* {"recentFriends":{"locations":["EIC","ZachShack"],"username":"Jim","sessionID":"hashedID"}}

*Server Message:* {"recentFriends":[{"location":"EIC","friends":[Method: openCV, Location: EIC, Time: 1000, Username: Alice,Method: openCV, Location: ZachShack, Time: 1001, Username: Bob,...]},{"location":"ZachShack",...]}]}

**Action:** Get friend's last five locations

*Client Message:* {"recentLocs":{"friends":["Alice","Bob","Charlie"],"username":"Jim","sessionID":"hashedID"}}

*Server Message:* {"recentLocs":[{"locations":[Method: openCV, Location: EIC, Time: 1000, Username: Alice,Method: openCV, Location: ZachShack, Time: 1001, Username: Bob,...],"username":"Alice"}, ,{"locations":[...],"username":"Bob"}]}

**Action:** Get friends

*Client Message:* {"selectFriends":{"username":"Jim","sessionID":"hashedID"}}

*Server Message:* {"friends":["Alice","Bob","Charlie","David","Elliot"]}

