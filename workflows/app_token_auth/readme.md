<!--METADATA
{
  "summary": "Learn how to authenticate using App Tokens"
}
-->

# App Token Authentication
An Application Token enables clients to provide their development partners or internal technical teams with restricted access to the Kaltura API. Each Application Token restricts the API methods that may be called by its users, and can allow restricted content access for clients who use entitlements (e.g., restricted access to MediaSpace content).

Developers who are provided with an Application Token use it to create temporary Kaltura Session (KS) tokens, which they will then use to access API functions. These KS tokens will have the restrictions of their originating Application Token.

## Start a Widget Session
Start an unprivileged session by calling `session.startWidgetSession`. Once the call completes, use client.setKs() to set the returned `ks` value as the KS (Kaltura Session) of the client object.

The widgetId should be a partnerId, with an underscore at the beginning, e.g. `_1234567`.

Do not set a KS on the client before making this call.

### API Call
```json
{
  "method": "get",
  "path": "/service/session/action/startWidgetSession",
  "disableSetupCode": true,
  "parameters": [
    {
      "name": "widgetId"
    }
  ]
}
```

## Compute the Hash
Compute a hash of the unprivileged Kaltura Session (from the previous step) and the App Token's value, concatenated. This value will be passed to `appToken.startSession` in the following step.

SHA-1 is the default hash function used with Application Tokens. If your Application Token was created with a different hash function (e.g. SHA-256 or MD5), compute that hash instead.

### API Call
```json
{
  "parameters": [
    {
      "name": "appTokenValue"
    },
    {
      "name": "hashFunction",
      "enum": [
        "sha1",
        "sha256"
      ],
      "default": "sha256"
    }
  ]
}
```
### Sample Code (node)
```javascript
var crypto = require('crypto');
var shasum = crypto.createHash('<%- answers.hashFunction %>');

client.ks = widgetSession.ks;
shasum.update(client.ks + "<%- answers.appTokenValue %>");
var hash = shasum.digest('hex');
```
### Sample Code (csharp)
```csharp
client.Ks = widgetSession.ks;
<% if (answers.hashFunction === 'sha1') { -%>
SHA1 sha = new SHA1CryptoServiceProvider();
<% } else { -%>
SHA256 sha = new SHA256CryptoServiceProvider();
<% } -%>
byte[] hash = sha.ComputeHash(Encoding.ASCII.GetBytes(client.Ks + "<%- answers.appTokenValue %>"));
string hashString = "";
foreach (char c in hash)
  hashString += string.Format("{0:x2}", (int)c);
```
### Sample Code (php)
```php
$client->setKS($widgetSession->ks);
<% if (answers.hashFunction === 'sha1') { -%>
$hashString = sha1($client->ks . "<%- answers.appTokenValue %>");
<% } else { -%>
$hashString = hash('sha256', $client->ks . "<%- answers.appTokenValue %>");
<% } -%>
```

### Sample Code (csharp)
```csharp
client.Ks = widgetSession.ks;
<% if (answers.hashFunction === 'sha1') { -%>
SHA1 sha = new SHA1CryptoServiceProvider();
<% } else { -%>
SHA256 sha = new SHA256CryptoServiceProvider();
<% } -%>
byte[] hash = sha.ComputeHash(Encoding.ASCII.GetBytes(client.Ks + "<%- answers.appTokenValue %>"));
string hashString = "";
foreach (char c in hash)
  hashString += string.Format("{0:x2}", (int)c);
```

### Sample Code (php)
```php
$client->setKS($widgetSession->ks);
<% if (answers.hashFunction === 'sha1') { -%>
$hashString = sha1($client->ks . "<%- answers.appTokenValue %>");
<% } else { -%>
$hashString = hash('sha256', $client->ks . "<%- answers.appTokenValue %>");
<% } -%>
```

### API Call
```json
{
  "parameters": [{
    "name": "appTokenValue"
  }, {
    "name": "hashFunction",
    "enum": ["sha1", "sha256"],
    "default": "sha256"
  }]
}
```

## Start the App Token Session
You will now call `appToken.startSession` to obtain a privileged Kaltura Session (KS). 

First, ensure that your client's KS is set to the unprivileged KS returned in Step 1.  Next, pass the following parameters into the `appToken.startSession` call:
* `id`: The ID of the Application Token with which you are authenticating.
* `tokenHash`: The hash value of the unprivileged KS and the Application Token value. Use the output of Step 2 of this recipe.
* `userId`: The ID of the user whose permissions will be used to access content. **Note that some Application Tokens already have a fixed User ID, in which case you do not need to pass this value.*

Executing the call returns a new, privileged KS for your application. Set the returned value as the client's KS.

You are now ready to make other API calls!

### API Call
```json
{
  "method": "get",
  "path": "/service/apptoken/action/startSession",
  "disableSetupCode": true,
  "parameters": [
    {
      "name": "ks",
      "description": "This should be the ks generated from startWidgetSession in step 1",
      "dynamicValue": {
        "fromStep": 0,
        "value": "ks"
      }
    },
    {
      "name": "id"
    },
    {
      "name": "tokenHash"
    },
    {
      "name": "userId"
    }
  ]
}
```
