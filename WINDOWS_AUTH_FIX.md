# Windows Authentication Issue Fix

## Problem
The Windows installer deployed via GitHub Actions was showing "username or password are wrong" errors, while the Mac version worked correctly.

## Root Cause
The issue was caused by **character encoding differences** between Windows and Mac platforms when encrypting passwords using `String.getBytes()` without specifying a charset. This can result in different byte arrays for the same password string on different platforms, leading to different Base64 encoded values.

## Changes Made

### 1. Fixed Password Encryption (CommonExtension.java)
- **Before**: `String.valueOf(jPasswordField.getPassword()).getBytes()`
- **After**: `String.valueOf(jPasswordField.getPassword()).getBytes(StandardCharsets.UTF_8)`
- **Impact**: Ensures consistent password encryption across all platforms

### 2. Enhanced Database Connection (persistence.xml)
- **Before**: `jdbc:mysql://switchback.proxy.rlwy.net:14492/railway`
- **After**: `jdbc:mysql://switchback.proxy.rlwy.net:14492/railway?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC`
- **Impact**: Ensures consistent character encoding in database operations

### 3. Added Debug Logging
- Added comprehensive logging in `LoginView.java`, `CompanyDAO.java`, and `JPAUtil.java`
- **Purpose**: Help identify authentication failures and debug issues

### 4. Created Password Test Utility
- New file: `PasswordTestUtility.java`
- **Purpose**: Test password encryption consistency across platforms

## Testing Instructions

### 1. Build and Deploy
```bash
mvn clean package -DskipTests
```

### 2. Test Password Encryption
Run the test utility to verify consistent encryption:
```bash
java -cp target/pchousestoremvn-1.0-SNAPSHOT-jar-with-dependencies.jar com.pchouse.pchousestoremvn.util.PasswordTestUtility
```

### 3. Check Debug Logs
When testing login on Windows, check the console output for:
- JPAUtil initialization messages
- Login attempt details
- Password encryption results
- Database connection status

### 4. Verify Database Connection
The debug logs will show:
- EntityManager creation success/failure
- Database query execution status
- Any connection errors

## Expected Results

After applying these fixes:
1. **Consistent Password Encryption**: Same password will produce identical Base64 encoding on Windows and Mac
2. **Proper Database Encoding**: UTF-8 encoding ensures proper character handling
3. **Better Error Visibility**: Debug logs will show exactly where authentication fails
4. **Cross-Platform Compatibility**: Authentication should work identically on both platforms

## Additional Recommendations

### For Production
1. **Remove Debug Logs**: Comment out or remove the `System.out.println` statements before production deployment
2. **Environment Variables**: Consider using environment variables for database credentials
3. **Error Handling**: Implement proper logging framework instead of console output

### For Future Development
1. **Consistent Charsets**: Always specify charset when converting strings to bytes
2. **Database Configuration**: Always include charset parameters in JDBC URLs
3. **Cross-Platform Testing**: Test on multiple platforms before deployment

## Files Modified
- `src/main/java/com/pchouse/pchousestoremvn/common/CommonExtension.java`
- `src/main/java/com/pchouse/pchousestoremvn/dao/CompanyDAO.java`
- `src/main/java/com/pchouse/pchousestoremvn/views/LoginView.java`
- `src/main/java/com/pchouse/pchousestoremvn/util/JPAUtil.java`
- `src/main/resources/META-INF/persistence.xml`
- `src/main/java/com/pchouse/pchousestoremvn/util/PasswordTestUtility.java` (new)

## Deployment
After making these changes, rebuild your Windows installer using the GitHub Actions workflow. The authentication should now work consistently across platforms.
