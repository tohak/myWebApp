<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

<h5>${username}</h5>
<#if message??>
    ${message}
</#if>
    <#if isAnonymous><a href="/user/reactivateemail">Reactivate email</a>

    </#if>
<form method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Old password:</label>
        <div class="col-sm-6">
            <input type="password" name="passwordold" class="form-control" placeholder="Password" />
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">New password:</label>
        <div class="col-sm-6">
            <input type="password" name="password" class="form-control" placeholder="New Password" />
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">New password2:</label>
        <div class="col-sm-6">
            <input type="password" name="password2" class="form-control" placeholder="New Password" />
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">New email:</label>
        <div class="col-sm-6">
            <input type="email" name="email" class="form-control" placeholder="some@some.com" value="${email!''}" />
        </div>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">Save</button>
</form>
    </@c.page>
