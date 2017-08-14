var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var FormItem = UcsmyUI.Form.FormItem;
var Grid = require('../widget/other/grid');

var PermissionLink = require('../widget/other/permissionLink');
var PermissionButton = require('../widget/other/permissionButton');
var Form = require('../widget/other/form');
var DatePicker = UcsmyUI.DatePicker;
myPanel = React.createClass({
	getInitialState:function () {
        return{
        
        }
    },
	 _query: function() {
	    	this.refs.grid.load({
	    		name: this.refs.name.getValue()
	    	});
	    },
	    handleClick:function(event){
            var tipE = React.findDOMNode(this.refs.tip); {/* 获取索引 */}
          
            if(tipE.style.display === 'none'){
                tipE.style.display = 'inline';
            }else{
                tipE.style.display='none';
            }
            event.preventDefault();
            event.stopPropagation();
        },
	   _delete: function(column, event) {
	    	var me = this;
			UcsmyIndex.confirm("确定", "你真的要删除该数据吗？", function() {
				$.post("workflow/process/delete", {deploymentId: column.deploymentId}, function(data) {
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
	    _suspend:function(column,event){
	    	 var me = this;
	    		$.post('workflow/process/update', {state: column.suspended,processDefinitionId:column.definitionId}, function (result) {
	                if (result && result.retcode && result.retcode == '0') {
	                    UcsmyIndex.alert("成功", result.retmsg);
	                    me.refs.grid.load();
	                } else {
	                    UcsmyIndex.alert("失败", result.retmsg);
	                    me.refs.grid.load();
	                }
	            });
	     },
	    _start:function(column,event){
	    	var me=this;
	    	UcsmyIndex.confirm("确定", "确定启动", function() {
				$.post("workflow/startprocess", {processDefinitionId: column.definitionId}, function(data) {
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
	    _save: function() {
	    	var me = this;
	    	// _addButtonDisabled('save');
	    	var formData = new FormData();
	    	var files = $("input[type='file'][id = applyProcess]" );
	    	if(files.prop('files')){
				var	file=files.prop('files')[0];
				if(!file){
					return;
				}  
				//var name = file.name;
				//alert(name);
				//var ext = name.substring(name.lastIndexOf(".") + 1);
				formData.append("files", file);
				
	    	}
	           
				$.ajax({
		            url: "workflow/deploy",
		            type: "post",
		            data: formData,
		            /**
					 * 必须false才会自动加上正确的Content-Type
					 */
		            contentType: false,
		            /**
					 * 必须false才会避开jQuery对 formdata 的默认处理 XMLHttpRequest会对 formdata
					 * 进行正确的处理
					 */
		            processData: false,
		            success: function (data) {
		            	me.refs.grid.reload();
		                if (data.retcode == "0") {
		                	 UcsmyIndex.alert("消息", data.retmsg);
		                }
		                else{
		                	UcsmyIndex.alert("异常消息", data.retmsg);
		                }
		            },
		            error: function (XMLHttpRequest, textStatus, errorThrown) {
		                UcsmyIndex.alert("异常消息", "文件上传失败，请检查文件");
		            }
		        });
			
	    },
	render: function() {
		var me = this;
		var divStyle = {
	            display: 'none'
	        };
		return (
			<div>
				<h2 className="content-title">流程定义</h2>
	            <div className="panel">
	            <div className="panel-title">查询条件</div>
	                <div className="panel-content">
	                 <FormItem label="流程名称"><Input ref="name"/></FormItem>
	            	</div>
	            </div>
	         
	            <div style={divStyle} ref="tip">
	            <input  type="file"  ref={"file"+"applyProcess"} name="file"  id="applyProcess"   className="displayHidden"/>
	           <input id="save" type="button" onClick={this._save} value="确定"/>
	            </div>
	            <div className="btn-panel">
	                <Button buttonType="bidnow" onClick={this._query}>查询</Button>
	                <PermissionButton permissionName="user_add" buttonType="bidnow" onClick={this.handleClick}>流程部署</PermissionButton>
	            </div>
				<div className="table-panel">
		            <Grid
		            	retDataProperty="data.resultList"
	            		retTotalProperty="data.totalCount"
	            		retCurrentProperty="data.pageNo"
		            	url="workflow/processlist" ref = "grid"
		                columns={[ 
         				    {
          						name: 'definitionId', header: '流程ID'
          					}, {
          						name: 'name', header: '流程名称'
          					},
          					{
          						name: 'version', header: '版本号'
          					},{
          						name: 'resourceName', header: 'XML',content:function(obj){
          							var xml=obj.resourceName;
          							var id=obj.definitionId;
         							 return (
                                            <span>
                                                <a target="_blank"  href={"workflow/resource/read?processDefinitionId="+id +"&resourceType=xml"} >{xml}</a>
                                            </span>
                                        )
         						}
          					},
          					{
          						name: 'diagramResourceName', header: '图片',content:function(obj){
          							var image=obj.diagramResourceName;
          							var id=obj.definitionId;
          							return (
          									
                                            <span>
                                                <a target="_blank"  href={"workflow/resource/read?processDefinitionId="+id +"&resourceType=image"} >{image}</a>
                                            </span>
                                        )
          						}
          					},
          					{
          						name: 'deploymentTime', header: '部署时间'
          					}, {
          						name: 'suspended', header: '是否挂起'
          					},{
          						name: 'cz',
          						header: '操作',
          						permissionName: 'user_delete',
          						content:function(column){
          							var state;
          							if(column.suspended=="true"){
          								state="激活"; 
          						   }else{
          							 state="挂起";
          						   }
          						
          							return (<span>
          							<PermissionLink permissionName="user_delete" href="Javascript:void(0);" onClick={me._delete.bind(this, column)}>删除</PermissionLink>&nbsp;
          		    		 		<PermissionLink permissionName="user_delete" href="Javascript:void(0);" onClick={me._suspend.bind(this,column)}>{state}</PermissionLink>

      				    		 	</span>)
          						}
          					}
          				]}
		            />
		            <div className="clearfix"></div>
		        </div>
			</div>
		);
	}
});