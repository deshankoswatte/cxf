/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.aegis.type.basic;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;

import org.apache.cxf.aegis.Context;
import org.apache.cxf.aegis.DatabindingException;
import org.apache.cxf.aegis.type.AegisType;
import org.apache.cxf.aegis.util.date.XsDateTimeFormat;
import org.apache.cxf.aegis.xml.MessageReader;
import org.apache.cxf.aegis.xml.MessageWriter;

/**
 * AegisType for the Time class which serializes to an xs:time.
 */
public class TimestampType extends AegisType {
    private static XsDateTimeFormat format = new XsDateTimeFormat();

    @Override
    public Object readObject(MessageReader reader, Context context) throws DatabindingException {
        String value = reader.getValue();

        if (value == null) {
            return null;
        }

        try {
            synchronized (format) {
                Calendar c = (Calendar)format.parseObject(value.trim());
                return new Timestamp(c.getTimeInMillis()); //NOPMD
            }
        } catch (ParseException e) {
            throw new DatabindingException("Could not parse xs:dateTime: " + e.getMessage(), e);
        }
    }

    @Override
    public void writeObject(Object object, MessageWriter writer, Context context) {
        Calendar c = Calendar.getInstance();
        c.setTime((Timestamp)object);
        synchronized (format) {
            writer.writeValue(format.format(c));
        }
    }
}
