var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var FormItem = UcsmyUI.Form.FormItem;
var Grid = require('../widget/other/grid');
var Form = require('./other/form');
var PermissionLink = require('../widget/other/permissionLink');
var PermissionButton = require('../widget/other/permissionButton');

myPanel = React.createClass({
    _reload: function () {
        this.refs.grid.reload();
    },
    _add: function () {
        var me = this;
        UcsmyIndex.openChildrenPage(Form, function (ref) {
            ref.init('config/addConfig', '添加参数', {}, function() {
            	me._reload();
            });
        });
    },
    _edit: function (item) {
        var me = this;
        UcsmyIndex.openChildrenPage(Form, function (ref) {
            ref.init('config/editConfig', '修改参数', item, function() {
            	me._reload();
            });
        });
    },
    _delete: function (item) {
        var me = this;
        UcsmyIndex.confirm("确定", "你真的要删除该参数数据吗？", function() {
        	$.post('config/deleteConfig', {id: item.id}, function (result) {
                if (result && result.retcode && result.retcode == '0') {
                    UcsmyIndex.alert("成功", result.retmsg);
                    me._reload();
                } else {
                    UcsmyIndex.alert("失败", result.retmsg);
                }
            });
		});
    },
    _search: function () {
        this.refs.grid.load({
            paramKey: this.refs.paramKey.getValue()
        });
    },
    render: function() {
        var me = this;

        return (
            <div>
                <h2 className="content-title">参数管理</h2>
                <div className="panel">
                    <div className="panel-title">查询条件</div>
                    <div className="panel-content">
                        <FormItem label="参数名称"><Input ref="paramKey"/></FormItem>
                    </div>
                </div>
                <div className="btn-panel">
                    <Button buttonType="bidnow" onClick={this._search}>查询</Button>
                    <PermissionButton permissionName="config_add" buttonType="bidnow"
                                      onClick={this._add}>添加</PermissionButton>
                </div>

                <div className="table-panel">
                    <Grid url="config/queryConfig" ref = "grid"
                          isTextOverflowHidden={true}
                          columns={[
                            {name: 'paramKey', header: '参数名称'},
                            {name: 'paramValue', header: '参数值', width: 300},
                            {name: 'paramDesc', header: '参数描述', width: 300},
                            {name: 'id', header: '操作', width: 200, content: function (item) {
                                return (
                                    <span>
                                        <PermissionLink permissionName="config_update" href="Javascript:void(0);" onClick={me._edit.bind(this, item)}>修改</PermissionLink>
                                        &nbsp;&nbsp;&nbsp;
                                        <PermissionLink permissionName="config_delete" href="Javascript:void(0);" onClick={me._delete.bind(this, item)}>删除</PermissionLink>
                                    </span>
                                )
                            }}
                        ]}>
                    </Grid>
                    <div className="clearfix"></div>
                </div>
            </div>
        );
    }
});