[#import "lib.ftl" as lib]
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
    <h1>Welcome, ${user}!</h1>
    <p>This is a benchmark test with ${count} iterations.</p>
    <p>Double the count: ${lib.double(count)}</p>
    
    [@lib.renderList items=items title="Items List"/]
    
    <div>
      [@lib.greet name=user/]
    </div>
    
    <footer>
        <p>Generated at: ${.now?string("yyyy-MM-dd HH:mm:ss")}</p>
    </footer>
</body>
</html>
