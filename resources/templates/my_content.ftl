[#-- Functions --]
[#function multiply a b]
  [#return a * b]
[/#function]

[#function toUpperCase str]
  [#return str?upper_case]
[/#function]

[#function formatNumber num]
  [#return num?string.number]
[/#function]

[#-- Macros --]
[#macro alert message type="info"]
  <div class="alert alert-${type}">
    <strong>Alert:</strong> ${message}
  </div>
[/#macro]

[#macro card title content]
  <div class="card">
    <div class="card-header">${title}</div>
    <div class="card-body">${content}</div>
  </div>
[/#macro]

[#macro timestamp]
  <time>${.now?string("yyyy-MM-dd HH:mm:ss")}</time>
[/#macro]
