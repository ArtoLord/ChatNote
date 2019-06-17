###Dataclasses
Message:
- ID
- Text
- Attachments
- Tags
- Time

Tag:
- ID
- Text

Attachment - JSON с любым типом вложения (в том числе ссылка на картинку)

###Database tables

Message:

- _id : long
- text : String
- time : long // date type

Tag:

- _id : long
- text : String

Attachment:

- _id : long
- type : String // Int
- link : JSONString
- parentId : long

Link:

- _id : long
- messageId : long
- tagId : long