import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {h} from "vue";
import {Tag} from "ant-design-vue";

export const columns: BasicColumn[] = [
  {
    title: '分类名称',
    dataIndex: 'categoryName',
    width: 160,
    align: 'left',
  },
  {
    title: '分类描述',
    dataIndex: 'categoryDesc',
    align: 'left',
  },
  {
    title: '类型',
    dataIndex: 'type',
    align: 'left',
    customRender: ({ record }) => {
      const type = record.type;
      let color = 'green';
      let text = '';
      if (type === 0) {
        color = 'green';
        text = '私有';
      } else if (type === 1) {
        color = 'magenta';
        text = '公共';
      }
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    dataIndex: 'sort',
    title: '排序',
    width: 160,
    align: 'left',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 180,
  },
  {
    title: '操作人',
    dataIndex: 'operator',
    width: 100,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'categoryName',
    label: '分类名称',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'categoryName',
    label: '分类名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'type',
    label: '类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '私有', value: 0 },
        { label: '公共', value: 1 },
      ],
    },
    required: true,
  },
  {
    field: 'parentId',
    label: '父分类',
    component: 'TreeSelect',
    componentProps: {
      replaceFields: {
        label: 'categoryName',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
  },
  {
    field: 'sort',
    label: '排序',
    component: 'InputNumber',
    required: true,
  },
  {
    label: '分类描述',
    field: 'categoryDesc',
    component: 'InputTextArea',
  },
];
