<#import "parts/common.ftl" as c>

<@c.page>
Password reset

<form method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">User name:</label>
        <div class="col-sm-6">
            <input type="text" name="username" class="form-control" placeholder="Login" />
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">email:</label>
        <div class="col-sm-6">
            <input type="email" name="email" class="form-control" placeholder="some@some.com" />
        </div>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">reset</button>
</form>

</@c.page>