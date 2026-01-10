<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
    <h1>Welcome, ${user}!</h1>
    <p>This is a benchmark test with ${count} iterations.</p>
    
    <h2>Items List:</h2>
    <ul>
    <#list items as item>
        <li>${item}</li>
    </#list>
    </ul>
    
    <footer>
        <p>Generated at: ${.now?string("yyyy-MM-dd HH:mm:ss")}</p>
    </footer>
</body>
</html>
