# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.clarity_detect import clarity_detect_aksk

if __name__ == '__main__':
    #
    # access moderation detect,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg'

    # call interface use the url
    result = clarity_detect_aksk(app_key, app_secret, "", demo_data_url, 0.8)
    print(result)

    # call interface use the file
    result = clarity_detect_aksk(app_key, app_secret, encode_to_base64('data/moderation-clarity-detect.jpg'), '', 0.8)
    print (result)