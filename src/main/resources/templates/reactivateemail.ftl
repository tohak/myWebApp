<#import "parts/common.ftl" as c>

<@c.page>
<h5>${username}</h5>
 <#if message??>
  ${message}
 </#if>
</@c.page>