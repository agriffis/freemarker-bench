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
    
    <div>
      [@my.alert message="Dynamic content loaded!" type="success"/]
      <p>Multiply count by 3: ${my.multiply(count, 3)}</p>
    </div>
    
    <footer>
        <p>Generated at: ${.now?string("yyyy-MM-dd HH:mm:ss")}</p>
    </footer>
</body>
</html>
