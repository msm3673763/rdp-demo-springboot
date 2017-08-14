var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var FormItem = UcsmyUI.Form.FormItem;
var Grid = require('../widget/other/grid');

var PermissionLink = require('../widget/other/permissionLink');
var PermissionButton = require('../widget/other/permissionButton');
var Form = require('../widget/other/form');
var DatePicker = UcsmyUI.DatePicker;
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
	formatDate: function(date,fmt) {
		var o = {
			"M+": date.getMonth() + 1, //月份
			"d+": date.getDate(), //日
			"h+": date.getHours(), //小时
			"m+": date.getMinutes(), //分
			"s+": date.getSeconds(), //秒
			"q+": Math.floor((date.getMonth() + 3) / 3), //季度
			"S": date.getMilliseconds() //毫秒
		};
		if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	},
	_getDate:function(dataTime)
	{
		var date = new Date(dataTime);
		return date;
	},
	 _query: function() {
		// alert(this.refs.startDate.getValue());
	    	this.refs.grid.load({
	    		name: this.refs.name.getValue(),
	    		date:this.refs.startDate.getValue()
	    	});
	    },
	    handleClick:function(event){
           
        },
	   _delete: function(column, event) {
	    
	    },
	    _start:function(column,event){
	    	alert('暂未实现');
	    },
	    _save: function() {
	    	
			
	    },
	render: function() {
		var me = this;
		
		return (
			<div>
				<h2 className="content-title">任务管理</h2>
	            <div className="panel">
	            <div className="panel-title">查询条件</div>
	                <div className="panel-content">
	                 <FormItem label="任务名称"><Input ref="name"/></FormItem>
	                 <FormItem label="任务逾期时间">
	                    <DatePicker ref="startDate" name="startDate" format="yyyy-mm-dd" time="true" />
	                 </FormItem>
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
		            	url="workflow/task/list" ref = "grid"
		                columns={[ 
         				    {
          						name: 'id', header: '任务ID'
          					},{
          						name: 'name', header: '任务名称'
          					},
          					{
          						name: 'processDefinitionId', header: '流程定义ID'
          					},{
          						name: 'processInstanceId', header: '流程实例ID'
          					},
          					{
          						name: 'createTime', header: '任务创建日期',content: function (obj) {
          							var  time=new Date(obj.createTime).Format("yyyy-MM-dd hh:mm:ss");
         							 return (
                                            <span>
                                                {time}
                                            </span>
                                        )
         						}
          					},{
          						name: 'owner', header: '任务所属人'
          					},{
          						name: 'assignee', header: '任务处理人'
          					},
          					{
          						name: 'cz',
          						header: '操作',
          						permissionName: 'user_delete',
          						content:function(column){
          							return (<span>
          							<PermissionLink permissionName="user_delete" href="Javascript:void(0);" onClick={me._start.bind(this, column)}>分配</PermissionLink>
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