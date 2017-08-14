var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var FormItem = UcsmyUI.Form.FormItem;
var Grid = require('../widget/other/grid');
var Layer=UcsmyUI.Layer;
var PermissionLink = require('../widget/other/permissionLink');
var PermissionButton = require('../widget/other/permissionButton');
var Form = require('../widget/other/form');
var DatePicker = UcsmyUI.DatePicker;
//var imageId;
myPanel = React.createClass({
	getInitialState:function () {
        return{
        	
        }
    },
    _handleClick:function(column,event){
    	//for ( var prop in column) 
    	//{ 
    	//alert("属性 '" + prop + "' 为 " +column[prop]); 
    	
    	//} 
    	//alert(column.id);
    	//imageId=column.id;
    	this.setState({imageId:column.id});

    	this.refs.layer2.layerOpen(); 
    },
    _query: function() {
    	this.refs.grid.load({
    		name: this.refs.name.getValue()
    	});
    },
    trace: function(){
    	
     },
     _suspend:function(column,event){
    	 var me = this;
    		$.post('workflow/process/update', {state: column.suspended,processDefinitionId:column.processDefinitionId}, function (result) {
                if (result && result.retcode && result.retcode == '0') {
                    UcsmyIndex.alert("成功", result.retmsg);
                    me.refs.grid.load();
                } else {
                    UcsmyIndex.alert("失败", result.retmsg);
                    me.refs.grid.load();
                }
            });
     },
	render: function() {
		var me = this;
		return (
			<div>
				<h2 className="content-title">流程实例</h2>
	            <div className="panel">
	            <div className="panel-title">查询条件</div>
	            <div className="panel-content">
                <FormItem label="流程名称"><Input ref="name"/></FormItem>
              	</div>
	            </div>
	            <div className="btn-panel">
	                <Button buttonType="bidnow" onClick={this._query}>查询</Button>
	             
	            </div>
				<div className="table-panel">
		            <Grid
		            	retDataProperty="data.resultList"
	            		retTotalProperty="data.totalCount"
	            		retCurrentProperty="data.pageNo"
		            	url="workflow/processinstance/running" ref = "grid"
		                columns={[ 
         				    {
          						name: 'id', header: '执行ID'
          					}, {
          						name: 'processInstanceId', header: '流程实例ID'
          					}, {
          						name: 'processDefinitionName', header: '流程定义名称'
          					},{
          						name: 'processDefinitionId', header: '流程定义ID'
          					},{
          						name: 'activityName', header: '当前节点',content:function(obj){
          							
          							var activityName=obj.activityName;
          							var pid=obj.id;
          							var pdid=obj.processDefinitionId;
         							 return (
                                            <span>
                                                <a  href="javascript:;" onClick={me._handleClick.bind(this,obj)} >{activityName}</a>
                                            </span>
                                        )
         						}
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
      				    		 		<PermissionLink permissionName="user_delete" href="Javascript:void(0);" onClick={me._suspend.bind(this,column)}>{state}</PermissionLink>
      				    		 	</span>)
          				    	 }
          					}
          				]}
		            />
		            <div>
                     <Layer ref="layer2" title="流程图"  width="900"  height="700">
                     
                     <div >
                          <img src={"/workflow/resource/process-instance?pid="+this.state.imageId+"&type=image"}/>
                     </div>
                
                  </Layer>
                      
				    </div>
				<div className="clearfix"></div>
		        </div>
		       
			</div>
			 
		);
	}
});


