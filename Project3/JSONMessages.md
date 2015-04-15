# JSON Messages

**Action:** Get friends

*Client Message:* {"selectFriends":"sampleUser"}

*Server Message:* {"friends":[{"username":"John0"},{"username":"John1"}]}

**Action:** Add new check in

*Client Message:* {"checkIn":{"method":"openCV","location":2,"time":1429112854192,"userName":"Bob"}}

*Server Message:* {"outcome":true}