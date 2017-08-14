var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var FormItem = UcsmyUI.Form.FormItem;
var Grid = require('../widget/other/grid');
var ModelForm = require('./other/modelForm');
var PermissionLink = require('../widget/other/permissionLink');
var PermissionButton = require('../widget/other/permissionButton');
var Form = require('../widget/other/form');
var DatePicker = UcsmyUI.DatePicker;
var Layer=UcsmyUI.Layer;
Date.prototype.Format = function (fmt) {  
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
myPanel = React.createClass({

    _query: function() {
    	this.refs.grid.load({
    		name: this.refs.name.getValue()
    	});
    },
    _open:function(){
    	this.refs.layer1.layerOpen(); 
    },
    _create: function(){
    	var me = this;
    	this.refs.layer1.layerClose();
    	var myname=this.refs.modelname.getValue();
    
    	var mykey=this.refs.mykey.getValue();
    	
    	var mydesc=this.refs.desc.getValue();
    	
    	$.post('workflow/create', {name: myname,key:mykey,description:mydesc}, function (result) {
    	
            if (result && result.retcode && result.retcode == '0') {
            	 me.refs.grid.reload();
            	 var url=""+result.data+"";
            	 window.open(url, '_blank');
            } else {
                UcsmyIndex.alert("失败", result.retmsg);
                
            }
        });
    	
    },
    _modelEdit:function(column,event){
    	
    	var url="explorer/modeler.html?modelId="+column.id+"";
    	
    	 window.open(url, '_blank');
    },
    _modelDeploy:function(column, event){
    	$.post('workflow/model/deploy', {modelId: column.id}, function (result) {
            if (result && result.retcode && result.retcode == '0') {
					UcsmyIndex.alert("成功", result.retmsg);
            } else {
                UcsmyIndex.alert("失败", result.retmsg);
                
            }
        });
    },
    _delete: function(column, event) {
    	var me = this;
		UcsmyIndex.confirm("确定", "你真的要删除该数据吗？", function() {
			$.post("workflow/model/delete", {modelId: column.id}, function(data) {
				if(data.retcode == 0) {
					UcsmyIndex.alert("成功", data.retmsg);
					me.refs.grid.reload();
				} else {
					UcsmyIndex.alert("失败", data.retmsg);
				}
			}, "json").error(function(xhr, errorText, errorType){
				UcsmyIndex.alert("失败", "网络异常");
		    });
		});
    },
	render: function() {
		var me = this;
		return (
			  <div>
				<h2 className="content-title">流程模型</h2>
	            <div className="panel">
	            <div className="panel-title">查询条件</div>
	                <div className="panel-content">
	                <FormItem label="流程名称"><Input ref="name"/></FormItem>
                    </div>
	            </div>
	            
	             <div className="btn-panel">
	                <Button buttonType="bidnow" onClick={this._query}>查询</Button>
	                <Button buttonType="bidnow" onClick={this._open}>创建</Button>
	            </div>
				<div className="table-panel">
		            <Grid
		            	retDataProperty="data.resultList"
	            		retTotalProperty="data.totalCount"
	            		retCurrentProperty="data.pageNo"
		            	url="workflow/model/list" ref = "grid"
		                columns={[ 
         				    {
          						name: 'id', header: '模型ID'
          				    	
          					}, {
          						name: 'name', header: '模型名称'
          					}, {
          						name: 'version', header: '版本号'
          					}, {
          						name: 'createTime', header: '创建时间',content: function (obj) {
          							var  time=new Date(obj.createTime).Format("yyyy-MM-dd hh:mm:ss");
          							 return (
                                             <span>
                                                 {time}
                                             </span>
                                         )
          						}
          					}, {
          						name: 'lastUpdateTime', header: '更新时间',content:function(obj){
          							var time=new Date(obj.lastUpdateTime).Format("yyyy-MM-dd hh:mm:ss");
          							 return (
                                             <span>
                                                 {time}
                                             </span>
                                         )
          						}
          					},
          					 {
          						name: 'metaInfo', header: '元数据'
          					},
          					{
          						name: 'cz',
          						header: '操作',
          						permissionName: 'user_delete',
              					content:function(column){
              					
              					return (<span>
              				      <PermissionLink permissionName="user_delete" href="Javascript:void(0);" onClick={me._modelEdit.bind(this, column)}>编辑</PermissionLink>&nbsp;
              				      <PermissionLink permissionName="user_delete" href="Javascript:void(0);" onClick={me._modelDeploy.bind(this, column)}>部署</PermissionLink>&nbsp;
              					  <PermissionLink permissionName="user_delete" href="Javascript:void(0);" onClick={me._delete.bind(this, column)}>删除</PermissionLink>
          				    		 	</span>)
              					}
          					}
          				]}
		            />
		            <div>
                    <Layer ref="layer1" title="创建模型"  width="400">
                      <div className="ucs-form-group">
                         <label className="label">名称：</label>
                         <div className="ucs-input-control">
                           <Input type="text" ref="modelname" className="ucs-input" placeholder="请输入名称"/>
                         </div>
                       </div>
                       <div className="ucs-form-group">
                         <label className="label">key：</label>
                         <div className="ucs-input-control">
                           <Input type="text" ref="mykey" className="ucs-input" placeholder="请输入key"/>
                          </div>
                       </div>
                       <div className="ucs-form-group">
                          <label className="label">描述：</label>
                          <div className="ucs-input-control">
                             <Input type="text" ref="desc" className="ucs-input" placeholder="请输入描述"/>
                           </div>
                       </div>
                       <div className="ucs-layer-footer">
                          <button className="ucs-btn ucs-btn-confirm" onClick={this. _create} data-layer="layer2">
                           确认
                       </button>
                   </div>
                 </Layer>
                     
				    </div>		
		            <div className="clearfix"></div>
		        </div>
			</div>
		);
	}
});