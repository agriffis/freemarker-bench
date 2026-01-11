[#-- Functions --]
[#function double num]
  [#return num * 2]
[/#function]

[#function capitalize str]
  [#return str?cap_first]
[/#function]

[#function sum a b]
  [#return a + b]
[/#function]

[#-- Macros --]
[#macro greet name]
  <div class="greeting">Hello, ${name}!</div>
[/#macro]

[#macro renderList items title]
  <div class="list-container">
    <h3>${title}</h3>
    <ul>
      [#list items as item]
        <li>${item}</li>
      [/#list]
    </ul>
  </div>
[/#macro]

[#macro badge text color="blue"]
  <span class="badge badge-${color}">${text}</span>
[/#macro]
