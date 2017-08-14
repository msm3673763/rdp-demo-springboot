var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var FormItem = UcsmyUI.Form.FormItem;
var SelectDropDown = UcsmyUI.SelectDropDown;
var Radio = UcsmyUI.Radio;
var RadioGroup = Radio.RadioGroup;
var Form = require('../../widget/other/form');
var Form = UcsmyUI.Form;
var FormItem = Form.FormItem;
module.exports = React.createClass({
    getInitialState: function() {
        return {
          
        }
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
        return (
            <div>
                <div className="panel">
                    <div className="panel-title fc-red">{this.state.title}</div>
                    <div className="panel-content">
                    <form name="saveForm"  id="saveForm" enctype="multipart/form-data" method="post">
                       <input  type="file"  ref={"file"+"applyProcess"} name="file"  id="applyProcess"   className="displayHidden"/> 
                    </form>
                    </div>
                </div>
                <div className="btn-panel">
                    <Button id="save" buttonType="bidnow" onClick={this._save}>确定</Button>
                    <Button buttonType="cancel" onClick={this._return}>取消</Button>
                </div>
            </div>
        );
    }
});