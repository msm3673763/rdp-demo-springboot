package com.ucsmy.ucas.manage.web;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.commons.utils.WorkflowUtils;







//import me.kafeitu.demo.activiti.util.Page;
//import me.kafeitu.demo.activiti.util.PageUtil;

//import me.kafeitu.demo.activiti.util.WorkflowUtils;

//import me.kafeitu.demo.activiti.util.WorkflowUtils;

//import me.kafeitu.demo.activiti.util.Page;
//import me.kafeitu.demo.activiti.util.PageUtil;

/**
 * 
 * @author ucs_hexuejun
 *
 */
@Controller
@RequestMapping(value = "/workflow")
public class ActivitiController {

	private static Logger logger = LoggerFactory.getLogger(ActivitiController.class);
	private static final String MESSAGE_SUCCESS = "工作流部署成功";
	private static final String MODEL_ID = "modelId";
	private static final String MODEL_NAME = "name";
	private static final String MODEL_REVISION = "revision";
	private static final String MODEL_DESCRIPTION = "description";
	@Autowired
	RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private FormService formService;

	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;

	@Autowired
	ProcessEngineFactoryBean processEngine;

	@Autowired
	ManagementService managementService;
	
	@Autowired
	private TaskService taskService;
	 
	private Map<String, Object> cacheProcessDefinition = new HashMap<String, Object>();

	private Map<String, String> cacheActivityName = new HashMap<String, String>();

	public int getFirstRecord(int pageNum,int pageSize){
		    int    first=((pageNum - 1) * pageSize) + 1;
		  return   first-1;
	 }
	/***
	 * 模型查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/model/list")
	@ResponseBody
	public AosResult modelList(@RequestParam(required = false) String name, int pageNum, int pageSize) {
		ModelQuery modelQuery = repositoryService.createModelQuery();
		List<Model> list = null;
		if (!"".equals(name) && name != null) {
			list = modelQuery.modelNameLike("%" + name + "%").listPage(getFirstRecord(pageNum,pageSize), pageSize);
		} else {
			list = modelQuery.listPage(getFirstRecord(pageNum,pageSize), pageSize);
		}
		long count = repositoryService.createModelQuery().count();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalCount", count);
		jsonObject.put("pageNo", pageNum);
		jsonObject.put("resultList", list);
		return AosResult.retSuccessMsg("工作流模型", jsonObject);
	}
	
	 /**
     * 根据Model部署流程
     */
    @RequestMapping(value = "model/deploy")
    @ResponseBody
    public AosResult deploy(@RequestParam(required = false) String modelId) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
            //redirectAttributes.addFlashAttribute("message", "部署成功，部署ID=" + deployment.getId());
            return AosResult.retSuccessMsg("部署成功", deployment);
        } catch (Exception e) {
            logger.error("根据模型部署流程失败：modelId={}", modelId, e);
            return AosResult.retSuccessMsg("部署失败");
        }
    }
	/***
	 * 删除模型
	 * 
	 * @param modelId
	 * @return
	 */
	@RequestMapping(value = "/model/delete")
	@ResponseBody
	public AosResult delete(@RequestParam("modelId") String modelId) {
		repositoryService.deleteModel(modelId);
		return AosResult.retSuccessMsg("流模型删除成功");
	}

	/**
	 * 流程定义列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/processlist")
	@ResponseBody
	public AosResult processList(@RequestParam(required = false) String name, HttpServletRequest request,
			@RequestParam(required = true) int pageNum, @RequestParam(required = true) int pageSize) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.orderByDeploymentId().desc();
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(getFirstRecord(pageNum,pageSize), pageSize);
		long count = processDefinitionQuery.count();
		List<Object> list = new ArrayList<>();
		JSONObject json = new JSONObject();
		for (ProcessDefinition processDefinition : processDefinitionList) {
			if (!"".equals(name) && name != null) {
				if (processDefinition.getName().contains(name)) {
					showData(list, processDefinition);
				}
			} else {
				showData(list, processDefinition);
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalCount", count);
		jsonObject.put("pageNo", pageNum);
		jsonObject.put("resultList", list);
		return AosResult.retSuccessMsg("流程定义", jsonObject);

	}

	private void showData(List<Object> list, ProcessDefinition processDefinition) {
		String deploymentId = processDefinition.getDeploymentId();
		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
		WorkflowDefine define = new WorkflowDefine();
		define.setDefinitionId(processDefinition.getId());
		define.setDeploymentId(deployment.getId());
		define.setName(processDefinition.getName());
		define.setKey(processDefinition.getKey());
		define.setVersion(processDefinition.getVersion());
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(deployment.getDeploymentTime());
		define.setDeploymentTime(date);
		define.setResourceName(processDefinition.getResourceName());
		define.setDiagramResourceName(processDefinition.getDiagramResourceName());
		define.setSuspended(processDefinition.isSuspended()== false ? "false" : "true");
	
		list.add(define);
	}

	
	
	/**
	 * 删除部署的流程，级联删除流程实例
	 *
	 * @param deploymentId
	 *            流程部署ID
	 */
	@RequestMapping(value = "/process/delete")
	@ResponseBody
	public AosResult deleteProcess(@RequestParam("deploymentId") String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return AosResult.retSuccessMsg("流程定义删除成功");
	}

	/**
	 * 读取资源，通过部署ID
	 *
	 * @param processDefinitionId
	 *            流程定义
	 * @param resourceType
	 *            资源类型(xml|image)
	 * @throws Exception
	 */
	@RequestMapping(value = "/resource/read")
	public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId,
			@RequestParam("resourceType") String resourceType, HttpServletResponse response) throws Exception {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String resourceName = "";
		if (resourceType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resourceType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}
		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				resourceName);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	@RequestMapping(value = "processinstance/running")
	@ResponseBody
	public AosResult running(@RequestParam(required = false) String name, HttpServletRequest request,
			@RequestParam(required = true) int pageNum, @RequestParam(required = true) int pageSize) {
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		List<ProcessInstance> list = null;
		if (!"".equals(name) && name != null) {
			list = processInstanceQuery.processDefinitionName(name).listPage(getFirstRecord(pageNum,pageSize), pageSize);
		} else {
			list = processInstanceQuery.listPage(getFirstRecord(pageNum,pageSize), pageSize);
		}

		List<WorkflowInstance> newList = new ArrayList<>();
		for (ProcessInstance p : list) {
			WorkflowInstance instance = new WorkflowInstance();
			instance.setId(p.getId());
			instance.setProcessInstanceId(p.getProcessInstanceId());
			instance.setProcessDefinitionName(p.getProcessDefinitionName());
			String definitionId = p.getProcessDefinitionId();
			instance.setProcessDefinitionId(definitionId);
			String activityName = getActivityName(definitionId, p.getActivityId());
			instance.setActivityName(activityName);
			instance.setSuspended(p.isSuspended() == false ? "false" : "true");
			newList.add(instance);
		}
		long count = processInstanceQuery.count();
		// page.setResult(list);
		// page.setTotalCount(processInstanceQuery.count());
		// mav.addObject("page", page);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalCount", count);
		jsonObject.put("pageNo", pageNum);
		jsonObject.put("resultList", newList);
		return AosResult.retSuccessMsg("OK", jsonObject);
	}

	/**
	 * 输出跟踪流程信息
	 *
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	/*
	 * @RequestMapping(value = "/process/trace")
	 * 
	 * @ResponseBody public List<Map<String, Object>>
	 * traceProcess(@RequestParam("pid") String processInstanceId) throws
	 * Exception { List<Map<String, Object>> activityInfos =
	 * traceService.traceProcess(processInstanceId); return activityInfos; }
	 */

	/**
	 * 读取带跟踪的图片
	 */
	/*@RequestMapping(value = "/process/trace/auto/{executionId}")
	public void readResource(@PathVariable("executionId") String executionId, HttpServletResponse response)
			throws Exception {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId)
				.singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		// 不使用spring请使用下面的两行代码
		// ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)
		// ProcessEngines.getDefaultProcessEngine();
		// Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

		// 使用spring注入引擎请使用下面的这行代码
		processEngineConfiguration = processEngine.getProcessEngineConfiguration();
		Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

		ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
		InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}*/

	/***
	 * 得到当前节环节名称
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public String getActivityName(String processDefinitionId, String activityId) {
		ProcessDefinition processDefinition = (ProcessDefinition) cacheProcessDefinition.get(processDefinitionId);
		if (processDefinition == null) {
			processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(processDefinitionId);
			cacheProcessDefinition.put(processDefinitionId, processDefinition);
		}
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) processDefinition;
		String activityName = cacheActivityName.get(activityId);
		if (activityName == null) {
			for (ActivityImpl activityImpl : pde.getActivities()) {
				String name = org.apache.commons.lang3.ObjectUtils.toString(activityImpl.getProperty("name"));
				cacheActivityName.put(activityImpl.getId(), name);
			}
		}
		return cacheActivityName.get(activityId);
	}

	/**
	 * 挂起、激活流程实例
	 */
	@RequestMapping(value = "process/update")
	@ResponseBody
	public AosResult updateState(@RequestParam("state") String state,
			@RequestParam("processDefinitionId") String processDefinitionId) {
		if (state.equals("true")) {
			repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
			return AosResult.retSuccessMsg("active");
		} else if (state.equals("false")) {
			repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
			return AosResult.retSuccessMsg("suspend");
		}
		return AosResult.retSuccessMsg("no");
	}
	
	@RequestMapping(value = "job/list")
	@ResponseBody
    public AosResult jobList(HttpServletRequest request,@RequestParam(required = true) int pageNum, 
    		@RequestParam(required = true) int pageSize) {
        JobQuery jobQuery = managementService.createJobQuery();
        long count=jobQuery.count();
        List<Job> jobList = jobQuery.listPage(getFirstRecord(pageNum,pageSize), pageSize);
        Map<String, String> exceptionStacktraces = new HashMap<String, String>();
        for (Job job : jobList) {
            if (StringUtils.isNotBlank(job.getExceptionMessage())) {
                exceptionStacktraces.put(job.getId(), managementService.getJobExceptionStacktrace(job.getId()));
            }
        }
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalCount", count);
		jsonObject.put("pageNo", pageNum);
		jsonObject.put("resultList", jobList);
        return AosResult.retSuccessMsg("", jsonObject);
    }
	
	   @RequestMapping(value = "task/list")
	   @ResponseBody
	    public AosResult taskList(@RequestParam(required = false) String name, @RequestParam(required = false) String date,@RequestParam(value = "processType", required = false) String processType,
	                                 HttpServletRequest request,@RequestParam(required = true) int pageNum, 
	                         		@RequestParam(required = true) int pageSize) {
		   
		   Date time=null;
		   try {
			   SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   if(date!=null){
			      time = sf.parse(date);
			   }
		   } catch (ParseException e) {
			
			e.printStackTrace();
		   }
		  
		   List<Task> taskList=null;
		   if(name==null){
			   name="";
		   }
			//if (!"".equals(name) && name != null) {
	             taskList=taskService.createTaskQuery().taskNameLike("%" + name + "%").taskDueBefore(time).listPage(getFirstRecord(pageNum,pageSize), pageSize);
			//}else {
				 //taskList=taskService.createTaskQuery().listPage(getFirstRecord(pageNum,pageSize), pageSize);
			//}
	        long count=taskService.createTaskQuery().count();
	      
	        List<TempTask> list=new ArrayList<>();
	        
	        for(Task task:taskList){
	        	TempTask tt=new TempTask();
	        	tt.setId(task.getId());
	        	tt.setTaskDefinitionKey(task.getTaskDefinitionKey());
	        	tt.setName(task.getName());
	        	tt.setProcessDefinitionId(task.getProcessDefinitionId());
	        	tt.setProcessInstanceId(task.getProcessInstanceId());
	        	tt.setCreateTime(task.getCreateTime());
	        	tt.setOwner(task.getOwner());
	        	tt.setAssignee(task.getAssignee());
	        
	        	list.add(tt);
	        }
	        JSONObject jsonObject = new JSONObject();
			jsonObject.put("totalCount", count);
			jsonObject.put("pageNo", pageNum);
			jsonObject.put("resultList", list);
	        return AosResult.retSuccessMsg("success",jsonObject);
	    }
	
	@RequestMapping(value = "/deploy")
	@ResponseBody
	public AosResult deploy(@RequestParam(value = "files", required = false) MultipartFile files) {
		String fileName = files.getOriginalFilename();
		Deployment deployment = null;
		try {
			InputStream fileInputStream = files.getInputStream();
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			}
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).list();
			for (ProcessDefinition processDefinition : list) {
				WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, "tmp/workflow");
			}
		} catch (Exception e) {
			logger.error("error on deploy process, because of file input stream", e);
		}

		return AosResult.retSuccessMsg(MESSAGE_SUCCESS, deployment);
	}

	

	@RequestMapping(value = "/create")
	@ResponseBody
	public AosResult createModel(@RequestParam("name") String name, @RequestParam("key") String key,
			@RequestParam("description") String description, HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ActivitiController.MODEL_NAME, name);
			modelObjectNode.put(ActivitiController.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ActivitiController.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
           String url="explorer/modeler.html?modelId="+modelData.getId();
          // System.out.println("---url----"+url);
			
           return AosResult.retSuccessMsg("success",url);
		} catch (Exception e) {
			logger.error("创建模型失败：", e);
			return AosResult.retSuccessMsg("fail");
		}
		
	}

	 /**
     * 读取资源，通过流程ID
     *
     * @param resourceType      资源类型(xml|image)
     * @param processInstanceId 流程实例ID
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/resource/process-instance")
    public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
            throws Exception {
        InputStream resourceAsStream = null;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(processInstance!=null){
	        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId())
	                .singleResult();
	
	        String resourceName = "";
	        if (resourceType.equals("image")) {
	            resourceName = processDefinition.getDiagramResourceName();
	        } else if (resourceType.equals("xml")) {
	            resourceName = processDefinition.getResourceName();
	        }
	        resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
	        byte[] b = new byte[1024];
	        int len = -1;
	        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
	            response.getOutputStream().write(b, 0, len);
	        }
        }
    }
	/**
	 * 提交启动流程
	 */
	@RequestMapping(value = "/startprocess")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public AosResult submitStartFormAndStartProcessInstance(
			@RequestParam("processDefinitionId") String processDefinitionId,

			HttpServletRequest request) {
		String processType = "all";
		Map<String, String> formProperties = new HashMap<String, String>();

		// 从request中读取参数然后转换
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			// fp_的意思是form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		User user = new User(); // UserUtil.getUserFromSession(request.getSession());
		user.setId("admin");
		user.setEmail("henry.yan@kafeitu.me");
		user.setFirstName("Henry");
		user.setLastName("Yan");
		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		// if (user == null || StringUtils.isBlank(user.getId())) {
		// return "redirect:/login?timeout=true";
		// }
		ProcessInstance processInstance = null;
		try {
			identityService.setAuthenticatedUserId(user.getId());
			processInstance = formService.submitStartFormData(processDefinitionId, formProperties);
			logger.debug("start a processinstance: {}", processInstance);
		} finally {
			identityService.setAuthenticatedUserId(null);
		}
		// redirectAttributes.addFlashAttribute("message", "启动成功，流程ID：" +
		// processInstance.getId());
		return AosResult.retSuccessMsg("OK");
		// return "redirect:/form/dynamic/process-list?processType=" +
		// processType;
	}

	private class WorkflowInstance {
		private String id;
		private String processInstanceId;
		private String processDefinitionName;
		private String processDefinitionId;
		private String activityName;
		private String suspended;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getProcessInstanceId() {
			return processInstanceId;
		}

		public void setProcessInstanceId(String processInstanceId) {
			this.processInstanceId = processInstanceId;
		}

		public String getProcessDefinitionId() {
			return processDefinitionId;
		}

		public void setProcessDefinitionId(String processDefinitionId) {
			this.processDefinitionId = processDefinitionId;
		}

		public String getActivityName() {
			return activityName;
		}

		public void setActivityName(String activityName) {
			this.activityName = activityName;
		}

		public String getSuspended() {
			return suspended;
		}

		public void setSuspended(String suspended) {
			this.suspended = suspended;
		}

		public String getProcessDefinitionName() {
			return processDefinitionName;
		}

		public void setProcessDefinitionName(String processDefinitionName) {
			this.processDefinitionName = processDefinitionName;
		}

	}

	private class WorkflowDefine {
		private String definitionId;
		private String deploymentId;
		private String name;
		private String key;
		private int version;
		private String resourceName;
		private String diagramResourceName;
		private String deploymentTime;
		private String suspended;
		
		public String getDefinitionId() {
			return definitionId;
		}

		public void setDefinitionId(String definitionId) {
			this.definitionId = definitionId;
		}

		public String getDeploymentId() {
			return deploymentId;
		}

		public void setDeploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public String getResourceName() {
			return resourceName;
		}

		public void setResourceName(String resourceName) {
			this.resourceName = resourceName;
		}

		public String getDiagramResourceName() {
			return diagramResourceName;
		}

		public void setDiagramResourceName(String diagramResourceName) {
			this.diagramResourceName = diagramResourceName;
		}

		public String getDeploymentTime() {
			return deploymentTime;
		}

		public void setDeploymentTime(String deploymentTime) {
			this.deploymentTime = deploymentTime;
		}

		public String getSuspended() {
			return suspended;
		}

		public void setSuspended(String suspended) {
			this.suspended = suspended;
		}

	}

	private class User {
		private String id;
		private String firstName;
		private String lastName;
		private String email;
		private String password;
		private boolean pictureSet;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public boolean isPictureSet() {
			return pictureSet;
		}

	}
	
	private class TempTask{
		private String id;
		private String taskDefinitionKey;
		private String name;
		private String processDefinitionId;
		private String processInstanceId;
		private Date createTime;
		private String owner;
		private String assignee;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTaskDefinitionKey() {
			return taskDefinitionKey;
		}
		public void setTaskDefinitionKey(String taskDefinitionKey) {
			this.taskDefinitionKey = taskDefinitionKey;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getProcessDefinitionId() {
			return processDefinitionId;
		}
		public void setProcessDefinitionId(String processDefinitionId) {
			this.processDefinitionId = processDefinitionId;
		}
		public String getProcessInstanceId() {
			return processInstanceId;
		}
		public void setProcessInstanceId(String processInstanceId) {
			this.processInstanceId = processInstanceId;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getAssignee() {
			return assignee;
		}
		public void setAssignee(String assignee) {
			this.assignee = assignee;
		}
		
	}
}
