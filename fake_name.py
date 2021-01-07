# -*- coding: utf-8 -*-
"""
Created on Thu Jan  7 00:42:01 2021

@author: hjp
"""

from faker import Faker

faker = Faker("en")
# names = []
# emails = []
# user_names = []
messages = []
for i in range(200):
    messages.append((faker.name(), faker.md5().upper(), faker.user_name(),
                     faker.email(), faker.date_time_between(start_date="-18M", end_date="now", tzinfo=None)))

file = "admin_list.dat"
with open(file, 'w+') as f:
    for message in messages:
        f.write(message[0]+','+message[1]+','+message[2]+ ','+message[3]+','+message[4].strftime("%Y-%m-%d %H:%M:%S")+'\n')
